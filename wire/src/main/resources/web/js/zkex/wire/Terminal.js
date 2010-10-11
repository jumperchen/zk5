/* Terminal.js

	Purpose:

	Description:

	History:
		Dec 16, 2009 11:02:08 AM , Created by joy
		2010/9/27 , updated by TonyQ

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

*/

(function () {

	var wireConfig = {}, wireDiv, fakeWire;

	function createTerminals(uuid, len, temporary, node, id) {
		var terminals = [],
			widget = zk.Widget.$(uuid),
			wc = widget.$class;
		if (len == 8)
			wireConfig = "n,s,e,w,nw,ne,sw,se".split(',');
		for (var i = 0; i < len; i++) {
			var element = document.createElement("div");
			if (id == "div_fakeWire") {  //if it's a temp div, this element won't display on the page
				jq(element).addClass("z-terminal-node z-fakeWire");
			} else {
				jq(element).addClass("z-terminal-node");
			}
			element.parentUuid = uuid;

			var scissor = document.createElement("div");
			jq(scissor).addClass("z-terminal-scissors");
			scissor.parentUuid = uuid;

			switch (wireConfig[i]) {
				case 'n':
					element.id = uuid + "n";
					element.style.left = (zk.parseInt(node.offsetWidth)/2 - 5) + "px";
					element.style.top = "-7px";
					scissor.id = element.id + "-scissor";
					scissor.style.left = "0px";
					scissor.style.top = "-16px";
					break;
				case 'w':
					element.id = uuid + "w";
					element.style.left = "-7px";
					element.style.top = (zk.parseInt(node.offsetHeight)/2 - 7) + "px";
					scissor.id = element.id + "-scissor";
					scissor.style.left = "-16px";
					scissor.style.top = "0px";
					break;
				case 's':
					element.id = uuid + "s";
					element.style.left = (zk.parseInt(node.offsetWidth)/2 - 5) + "px";
					element.style.top = zk.parseInt(node.offsetHeight)-10 + "px";
					scissor.id = element.id + "-scissor";
					scissor.style.left = "-5px";
					scissor.style.top = "10px";
					break;
				case 'e':
					element.id = uuid + "e";
					element.style.left = zk.parseInt(node.offsetWidth)-10 + "px";
					element.style.top = (zk.parseInt(node.offsetHeight)/2 - 7) + "px";
					scissor.id = element.id + "-scissor";
					scissor.style.left = "10px";
					scissor.style.top = "0px";
					break;
				case 'nw':
					element.id = uuid + "nw";
					element.style.left = "-7px";
					element.style.top = "-7px";
					scissor.id = element.id + "-scissor";
					scissor.style.left = "0px";
					scissor.style.top = "-16px";
					break;
				case 'ne':
					element.id = uuid + "ne";
					element.style.left = zk.parseInt(node.offsetWidth)-10 + "px";
					element.style.top = "-7px";
					scissor.id = element.id + "-scissor";
					scissor.style.left = "0px";
					scissor.style.top = "-16px";
					break;
				case 'sw':
					element.id = uuid + "sw";
					element.style.left = "-7px";
					element.style.top = zk.parseInt(node.offsetHeight)-10 + "px";
					scissor.id = element.id + "-scissor";
					scissor.style.left = "-5px";
					scissor.style.top = "10px";
					break;
				case 'se':
					element.id = uuid + "se";
					element.style.left = zk.parseInt(node.offsetWidth)-10 + "px";
					element.style.top = zk.parseInt(node.offsetHeight)-10 + "px";
					scissor.id = element.id + "-scissor";
					scissor.style.left = "-5px";
					scissor.style.top = "10px";
					break;
				default:
					break;
			}

			new zk.Draggable(widget, element, {
				change: _drawing,
				ghosting: _ghostDrag,
				endghosting: _endGhost,
				endeffect: _endDrag
			});

			element.appendChild(scissor);
			jq(temporary).append(element);
			terminals.push(element);
		}
	};

	function _drawing(dg, pointer, evt) {
		var domTarget = evt.domTarget,
			control = dg.control;

		if (fakeWire) {
			control.parent.removeChild(fakeWire);
			fakeWire = null;
		}
		if (wireDiv && !fakeWire) {
			wireDiv.style.left = zk.parseInt(evt.pageX) + "px";
			wireDiv.style.top = zk.parseInt(evt.pageY) + "px";
			fakeWire = new wire.Wire({_inId:control.id, _outId:'div_fakeWire',
				_joint:(dg.handle.id).toString().split(control.uuid)[1] + ",n", _config:'drawingMethod=bezier'});
			control.parent.appendChild(fakeWire);
			fakeWire._element.className = "z-fakeWire-zIndex";
		}
	};

	function _ghostDrag(dg, ofs, evt) {
		var control = dg.control,
			el = dg.node;
//			div = new wire.Wirewindow();
//		div.setId('div_fakeWire');  //create a ghost div for wire
//		div.setWirable('n');
//		div.setDraggable('true');
//		div.setStyle("position:absolute;width:10px;height:10px;");
		jq(document.body).prepend(
			'<div id="zk_fake" style="position:absolute;width:10px;height:10px;" />'
		);	//append a ghost div for dragging
//		control.parent.appendChild(div);
		//wireDiv = div.$n();
		return jq('#zk_fake')[0];
	};

	function _endGhost(dg, evt) {
		var ghost = jq('#zk_fake')[0];
		if (ghost)
			jq('#zk_fake').remove(); //remove ghost div
		if (wireDiv)
			jq(wireDiv).remove(); //remove ghost div for wire
	};

	function _endDrag(dg, evt) {
        if(fakeWire)    dg.control.parent.removeChild(fakeWire); //always remove ghost wire
//		fakeWire = null;

		var domTarget = evt.domTarget,
			uuid = domTarget.parentUuid,
			control = dg.control;
		if (control.id && domTarget.parentNode) {  //if match these conditions, widget will fire a onWire event to server
			control.fire("onWire", {
				domtargetId: domTarget.id,
				uuid: uuid,
				sourceId: control.id,
				targetId: domTarget.parentNode.parentId,
				joints: (dg.handle.id).toString().split(control.uuid)[1] + "," + (domTarget.id).toString().split(uuid)[1],
				config: 'drawingMethod=bezier'
			}, {toServer: true});
		}
	}

zkex.wire.Terminal = zk.$extends(zk.Widget, {
	$init: function (uuid, id, wirable, n, width, height) {
		var element = document.createElement("div"),
			parent = n.parentNode;
		if (!width || !height) {
			element.style.width = "0px";
			element.style.height = "0px";
		} else {
			element.style.width = width + "px";
			element.style.height = height + "px";
		}
		element.setAttribute("class", "z-terminal");
		element.parentId = id;

		wireConfig = wirable.split(',');
		if (wireConfig[0] == 'true') {
			createTerminals(uuid, 8, element, n, id);
		} else {
			createTerminals(uuid, wireConfig.length, element, n, id);
		}

		jq(n).append(element);
		this._element = element;
	},

	addListen_: function (wgt) {
		var uuid = wgt.uuid,
			list = jq("div#" + uuid + " div.z-terminal-node"),
			len = list.length;
		for (var i = 0; i < len; i++) {
			this.domListen_(list[i], 'onMouseOver', '_terminalMouseOver')
				.domListen_(list[i], 'onMouseOut', '_terminalMouseOut');
		}

		list = jq("div#" + uuid + " div.z-terminal-node div"),
		len = list.length;
		for (var i = 0; i < len; i++) {
			this.domListen_(list[i], 'onClick', '_scissorClick')
				.domListen_(list[i], 'onMouseOver', '_scissorMouseOver')
				.domListen_(list[i], 'onMouseOut', '_scissorMouseOut');
		}
	},

	removeListen_: function (wgt) {
		var uuid = wgt.uuid,
			list = jq("div#" + uuid + " div.z-terminal-node"),
			len = list.length;
		for (var i = 0; i < len; i++) {
			this.domUnlisten_(list[i], 'onMouseOver')
				.domUnlisten_(list[i], 'onMouseOut');
		}

		list = jq("div#" + uuid + " div.z-terminal-node div"),
		len = list.length;
		for (var i = 0; i < len; i++) {
			this.domUnlisten_(list[i], 'onClick')
				.domUnlisten_(list[i], 'onMouseOver')
				.domUnlisten_(list[i], 'onMouseOut');
		}
	},

	_terminalMouseOver: function (evt) {
		var scissor = evt.domTarget.firstChild;
		if (scissor)
			scissor.style.display = 'inline';
	},

	_terminalMouseOut: function (evt) {
		var scissor = evt.domTarget.firstChild;
		if (scissor)
			scissor.style.display = 'none';
	},

	_scissorClick: function (evt) {  //every time we click scissor, it will call a sync method to clean all the wires which was linked on this widget
		wire.Wirer.sync(zk.Widget.$(evt.domTarget.parentUuid), evt, true);
	},

	_scissorMouseOver: function (evt) {
		var scissor = evt.domTarget;
		if (scissor)
			scissor.style.display = 'inline';
	},

	_scissorMouseOut: function (evt) {
		var scissor = evt.domTarget;
		if (scissor)
			scissor.style.display = 'none';
	}

});
})();