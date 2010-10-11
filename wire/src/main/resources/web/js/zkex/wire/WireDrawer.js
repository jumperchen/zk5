//function getXY(terminal, parentNode, joint) {
//}

//function setDirection(isTargetNode, joint) {
//}


function _createConvas(id, zclass, point, width, height) {
    var convas = zk.canvas.Canvas.create(width, height);
    convas.id = id;
    convas.setAttribute('class', zclass);
    if (typeof(G_vmlCanvasManager) != "undefined") convas = G_vmlCanvasManager.initElement(convas);
    if (zk.ie) {
        jq(convas).css({
            left: point[0] + "px",
            top: point[1] + "px"
        });

        // this is old code from joy,need to check why we need this and comment it . by TonyQ
        convas.getContext("2d").clearRect(0, 0, width, height);
    } else {
        jq(convas).css({
            left: point[0] + "px",
            top: point[1] + "px"
        });
    }
    return convas;//convas.getContext("2d")
}

function _getDrawDetail(from, to) {
    var margin = [4, 4], min = [Math.min(from[0], to[0]) - margin[0], Math.min(from[1], to[1]) - margin[1]], max = [Math.max(from[0], to[0]) + margin[0], Math.max(from[1], to[1]) + margin[1]], width = Math.abs(max[0] - min[0]), height = Math.abs(max[1] - min[1]), newFrom = [], newTo = [];


    newFrom[0] = from[0] - min[0];
    newFrom[1] = from[1] - min[1];
    newTo[0] = to[0] - min[0];
    newTo[1] = to[1] - min[1];

    return {
        relative_nw: newFrom,
        relative_se: newTo,
        width: width,
        height: height,
        point_nw: min,
        point_se: max
    };
}

/**
 *
 * @param {Object} ctxt
 * @param {Object} conf {color,bordercolor,borderwidth}
 * @param {Object} p1
 * @param {Object} p2
 * @param {Object} p3
 */
function _drawTriangle(ctxt,conf, p1, p2, p3) {
    //triangle fill
    _fill(ctxt,{fillcolor:conf["color"]},[p1,p2,p3]);
    //triangle border
    _drawPolyline(ctxt,[p1,p2,p3,p1],
        {
             color:conf['bordercolor'],
             width:conf['borderwidth']
        }
    );
}

/**
 *
 * @param {Object} ctxt
 * @param {Object} conf
 * @param {Array} points
 */
function _fill(ctxt, conf, points) {
    _conf(ctxt, conf);
    ctxt.beginPath();
    ctxt.moveTo(points[0][0], points[0][1]);
    for (var i = 1, len = points.length; i < len; ++i) {
        ctxt.lineTo(points[i][0], points[i][1]);
    }
    ctxt.fill();

}

function _conf(ctxt, conf) {
    if (conf.fillcolor) ctxt.fillStyle = conf.fillcolor;
    if (conf.cap) ctxt.lineCap = conf.cap;
    if (conf.color) ctxt.strokeStyle = conf.color;
    if (conf.width) ctxt.lineWidth = conf.width;
}
/**
 *
 * @param {Object} ctxt
 * @param {Object} points
 * @param {Object} lineconf
 */
function _drawPolyline(ctxt, points, lineconf) {
    _conf(ctxt,lineconf);
    for (var i = 0, len = points.length; i < len - 1; ++i) {
        _drawline(ctxt, points[i], points[i] + 1, lineconf);
    }
}

function _drawline(ctxt, start, to, lineconf, borderconf) {
    if (borderconf) {
        // Draw the border if exist
        var ctxborderconf = {};
        zk.copy(ctxborderconf, borderconf);
        ctxborderconf["width"] = zk.parseInt(lineconf["width"]) + zk.parseInt(borderconf["width"]) * 2;
        _drawline(ctxt, start, to, ctxborderconf);
    }

    if (lineconf) {
        _conf(ctxt, lineconf);
    }
    //draw the line
    ctxt.beginPath();
    ctxt.moveTo(start[0], start[1]);
    ctxt.lineTo(to[0], to[1]);
    ctxt.stroke();
}

/*
 * Draw straight wire, copy from wireIt. Logical is same with wireIt, but some attributes had been changed.
 */
function drawStraight(wgt, target, source, joints, parentNode) {
    if (wgt._element) {
        parentNode.removeChild(wgt._element);
    }
    var margin = [4, 4], p1 = getXY(target, joints[0]), p2 = getXY(source, joints[1]), drawdetail = _getDrawDetail(p1, p2), convas = _createConvas(wgt.uuid, "z-canvas", drawdetail.point_nw, drawdetail.width, drawdetail.height), ctxt;

    p1 = drawdetail.relative_nw;
    p2 = drawdetail.relative_se;

    parentNode.appendChild(convas);
    ctxt = convas.getContext("2d");
    wgt._element = convas;

    // Draw the border
    _drawline(ctxt, p1, p2, {
        width: _configs['width'],
        cap: _configs['cap'],
        color: _configs['color']
    }, {
        width: _configs['borderwidth'],
        cap: _configs['bordercap'],
        color: _configs['bordercolor']
    });
}
