var colors = {
  before: 'red',
  current: 'green',
  after: 'blue',
  normal: 'grey',
  owned: '#E3A003',

  getDefaultColor: function (item) {
    return item.own ? colors.owned : colors.normal
  }
}

var fileValue = (new URL(document.location)).searchParams.get("file");
var fileName;
if(fileValue == null){
  fileName = 'input.json';
}
else{
  var element = document.getElementById('selector');
  element.value = fileValue;

  switch(fileValue){
    case 'dendogramme':
      fileName = 'inputDendogramme.json'
      break;
    case 'couplage':
      fileName = 'inputCouplage.json'
      break;
    default:
      fileName = 'input.json'
      break;
  }
}

var config = {
  nodeRadius: 20,
  forceManyStr: -400
}

var svg = d3.select('body').
  append('svg').
  attr('width', '100%').
  attr('height', '100%').
  call(d3.zoom().on('zoom', function () {
    svg.attr('transform', d3.event.transform)
  })).
  append('g')

var width = d3.select('body').node().getBoundingClientRect().width,
  height = d3.select('body').node().getBoundingClientRect().height

d3.json(fileName, function (error, graph) {
  if (error) throw error

  window.document.getElementById('charge').value = config.forceManyStr

  var simulation = d3.forceSimulation(graph.nodes).
    force('charge', d3.forceManyBody().strength(-400).
      distanceMax(config.nodeRadius * 40)).
    force('nocollision',
      d3.forceCollide(config.nodeRadius * 2).strength(0.3)).
    force('forceX', d3.forceX(width / 2).strength(0.02)).
    force('forceY', d3.forceY(height / 2).strength(0.02)).
    //force('center', d3.forceCenter(width / 2, height / 2)).
    force('link', d3.forceLink(graph.links).
      id(function (d) { return d.id }).distance(function (link) {
        return (2 - link.str) * 4 * config.nodeRadius
      }).strength(function (link) {
        return link.str
      })).
    on('tick', ticked)

  var events = {
    dragStarted: function (d) {
      if (!d3.event.active) simulation.alphaTarget(0.3).restart()
      d.fx = d.x
      d.fy = d.y
    },

    dragged: function (d) {
      d.fx = d3.event.x
      d.fy = d3.event.y
    },

    dragEnded: function (d) {
      if (!d3.event.active) simulation.alphaTarget(0)
      d.fx = null
      d.fy = null
    }
  }

  svg.append('svg:defs').
    append('svg:marker').
    attr('id', 'triangle').
    attr('refX', 5).
    attr('refY', 5).
    attr('markerWidth', 10).
    attr('markerHeight', 10).
    attr('orient', 'auto').
    append('path').
    attr('d', 'M 0 0 10 5 0 10 2.5 5').
    style('stroke', '#999')

  var links = svg.append('g').
    attr('class', 'links').
    selectAll('path').
    data(graph.links).
    enter().
    append('path').
    attr('marker-end', 'url(#triangle)').
    attr('stroke-width', function(d){
      if(d.weight){  
        return d.weight
      }
      return 2;
    })

  links.append("svg:title").text(function(d) {
    if(d.weight){
      return 'Weight : ' + d.weight; 
    } else{  
      return 'Weight : ' + d.str;
    }
  });

  var selection = {

    current: null,

    node: function (node) {
      // if selected before
      var toSelect = selection.current === null
      if (!toSelect) { // we need to deselect the actual one
        // if it's a different selection than the actual one
        toSelect = node.id !== selection.current.id
        // deselecting all the actual objects
        _.forEach(selection.current.origin, function (item) {
          item.attr('fill', colors.getDefaultColor)
        })
        _.forEach(selection.current.target, function (item) {
          item.attr('fill', colors.getDefaultColor)
        })

        selection.current.node.attr('fill', colors.getDefaultColor)

        selection.current = null
      }
      if (toSelect) {
        // selecting object
        selection.current = selection.getRelations(graph.links, node)

        _.forEach(selection.current.origin, function (item) {
          item.attr('fill', colors.before).
            raise()
        })
        _.forEach(selection.current.target, function (item) {
          item.attr('fill', colors.after).
            raise()
        })

        selection.current.node.attr('fill', colors.current).
          raise()
      }
    },

    getRelations: function (links, node) {
      return _.reduce(links, function (relations, link) {
        if (link.source.id === node.id) {
          relations.target.push(d3.select(link.target.self))
        } else if (link.target.id === node.id) {
          relations.origin.push(d3.select(link.source.self))
        }
        return relations
      }, {origin: [], target: [], node: d3.select(node.self), id: node.id})
    }
  }

  var nodeGroups = svg.append('g').
    attr('class', 'nodes').
    selectAll('circle').
    data(graph.nodes).
    enter().
    append('g').
    attr('fill', colors.getDefaultColor).
    call(d3.drag().
      on('start', events.dragStarted).
      on('drag', events.dragged).
      on('end', events.dragEnded)).
    on('click', selection.node).call(function (ng) {
      ng.each(function (item) {
        item.self = this
      })
    })

  var nodes = nodeGroups.append('circle').
    attr('r', config.nodeRadius)

  var nodeTexts = nodeGroups.append('text').
    text(function (node) {
      return node.name
    }).
    attr('font-size', 15).
    attr('dx', -config.nodeRadius - 5).
    attr('dy', 5).
    attr('fill', 'white').
    call(getBB)

  var nodeBgnd = nodeGroups.insert('rect', 'text').
    attr('width', function (d) {return d.bbox.width}).
    attr('height', function (d) {return d.bbox.height}).
    attr('rx', 5).
    attr('ry', 5)

  nodeGroups.append('title').text(function (d) { return d.id + ':' + d.name })

  function getBB (selection) {
    selection.each(function (d) {d.bbox = this.getBBox()})
  }

  // simulation.alphaTarget(0.3)

  function ticked () {
    links.attr('d', function (d) {
      var x1 = d.source.x,
        y1 = d.source.y,
        x2 = d.target.x,
        y2 = d.target.y,

        // Defaults for normal edge.
        drx = 0,
        dry = 0,
        xRotation = 0, // degrees
        largeArc = 0, // 1 or 0
        sweep = 1 // 1 or 0

      // Self edge.
      if (x1 === x2 && y1 === y2) {
        // Fiddle with this angle to get loop oriented.
        xRotation = -45

        // Needs to be 1.
        largeArc = 1

        // Change sweep to change orientation of loop.
        //sweep = 0;

        // Make drx and dry different to get an ellipse
        // instead of a circle.
        drx = 20
        dry = 20

        // For whatever reason the arc collapses to a point if the beginning
        // and ending points of the arc are the same, so kludge it.
        x2 = x2 + 1
        y2 = y2 + 1
      }

      var dx = x2 - x1,
        dy = y2 - y1,
        dr = Math.sqrt(dx * dx + dy * dy)

      var normX = dx / dr,
        normY = dy / dr
      x2 = x2 - ((config.nodeRadius + 12) * normX)
      y2 = y2 - ((config.nodeRadius + 12) * normY)

      return 'M' + x1 + ',' + y1 + 'A' + drx + ',' + dry + ' ' + xRotation +
        ',' + largeArc + ',' + sweep + ' ' + x2 + ',' + y2
    })
    nodeBgnd.attr('x', function (d) { return d.x - config.nodeRadius - 5 }).
      attr('y', function (d) { return d.y - 7 })
    nodeTexts.attr('x', function (d) { return d.x }).
      attr('y', function (d) { return d.y })
    nodes.attr('cx', function (d) { return d.x }).
      attr('cy', function (d) { return d.y })
  }

window.document.getElementById('display').
    addEventListener('change', function () {
      if (this.checked) {
        links.classed('hide', function (l) {
          return !l.source.own || !l.target.own
        })
        nodeGroups.classed('hide', function (d) {
          return !d.own
        })
      } else {
        links.classed('hide', false)
        nodeGroups.classed('hide', false)
      }
    }, false)

  window.document.getElementById('charge').
    addEventListener('change', function () {
      config.forceManyStr = this.value
      simulation.force('charge', d3.forceManyBody().
        strength(config.forceManyStr).
        distanceMax(config.nodeRadius * 40))
      simulation.alphaTarget(0.3).restart()
    })
})

window.document.getElementById('selector').addEventListener('change', function(ev){
  var value = this.options[this.selectedIndex].value;
  window.location.href = '?file=' + value;
})
