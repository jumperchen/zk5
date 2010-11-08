/*
 * wire.css.dsp
 *
 * Purpose:
 *
 * Description:
 *
 * History: 2010/10/6, Created by TonyQ
 *
 * Copyright (C) 2010 Potix Corporation. All Rights Reserved.
 */
 <%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

canvas {
	position: absolute;
	z-index: -10;
	*z-index: -1;
}

.z-wirebox{
	padding:10px;  /*padding for terminal*/
	position:relative;
}

.z-wirebox {
	z-index: 50;
}
div.z-wire-over{
	border:2px solid yellow;
	background-color:red;
}
.z-wireable{
	cursor:pointer;
}
.z-wirebox-point {
	position: absolute;
	width: 10px;
	height: 10px;
	background-color: #EEEEEE;
	border: solid 1px #1111FA;
	display: block;
	z-index: 70;
}

.z-canvas {
	background-color: transparent;
	border: 0;
}

.z-wirebox-scissors {
	border: 0;
	position: absolute;
	width: 16px;
	height: 16px;
	background-color: transparent;
	background-image: url(${c:encodeURL('~./img/zkex/wire/cut.png')});
	cursor: pointer;
	display: none;
	z-index: 90;
}

.z-wirable-window, .z-wirable-panel {
	overflow: visible;
}