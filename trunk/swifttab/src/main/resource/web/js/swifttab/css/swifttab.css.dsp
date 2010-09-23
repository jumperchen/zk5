<%--
swifttab.css.dsp

	Purpose:

	Description:

	History:
		2010/09/20, Created by TonyQ

Copyright (C) 2010 Potix Corporation. All Rights Reserved.

This program is distributed under GPL Version 3.0 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
--%><%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>


li.z-swifttab {
	cursor: pointer;
}

li.z-swifttab-seld {
	cursor: default;
}

.z-swifttab .z-swifttab-link,.z-swifttab .z-swifttab-text,.z-swifttab .z-swifttab-sort {
	background: url("${c:encodeURL('~./img/swifttab/swifttab-bg.png')}") no-repeat scroll 0px 0px transparent;
	position: relative;
}
.z-swifttab .z-swifttab-sort {
	background-position: -112px -80px;
	cursor: move;
	display: block;
	height: 15px;
	width: 20px;
	position:absolute;
}

.z-swifttab .z-swifttab-content {
	padding-left: 20px;
}
.z-swifttab .z-swifttab-link {
	color: #27537A;
	line-height: 1.2;
	margin: 1px 0 0;
	background-position: 100% -129px;
	outline: 0 none;
	text-align: center;
	text-decoration: none;
	white-space: nowrap;
	display: block;
	padding: 0 15px 0 0;
}

.z-swifttab .z-swifttab-text {
	color: #416AA3;
	background-position: 0 -322px;
	padding-left:20px;
}

.z-swifttab-seld a.z-swifttab-link:link,
.z-swifttab-seld a.z-swifttab-link:visited,
.z-swifttab-seld a.z-swifttab-link:visited {
	cursor: text;
}

.z-swifttab-seld .z-swifttab-text {
	background-position: 0 -193px;
	color: #0F3B82;
}

.z-swifttab a.z-swifttab-link:hover .z-swifttab-text {
	background-position: 0 -258px;
}

.z-swifttab-seld a.z-swifttab-link:hover .z-swifttab-text {
	background-position: 0 -193px;
	color: #0F3B82;
}

.z-swifttab-seld .z-swifttab-link {
	background-position: 100% 0px;
	color: #000000;
	margin-top: 0;
	position: relative;
	top: 1px;
	z-index: 2;
	font-weight: bold;
}

.z-swifttab a.z-swifttab-link:hover {
	background-position: 100% -65px;
}

.z-swifttab-seld a.z-swifttab-link:hover {
	background-position: 100% 0px;
}

.z-swifttab .z-swifttab-text {
	height: auto;
	width: auto;
	margin: 0 0 0 1px;
	text-align: center;
	min-height: 18px;
	min-width: 64px;
	padding: 6px 0px 0 10px;
	display: block;
}

.z-swifttab-close,.z-swifttab-ver-close {
	background-image: url("${c:encodeURL('~./zul/img/tab/tab-close.gif')}");
	background-repeat: no-repeat;
	cursor: pointer;
	display: block;
	height: 12px;
	opacity: 0.8;
	position: absolute;
	right: 3px;
	top: 3px;
	width: 11px;
	z-index: 15;
}

.z-swifttab-close:hover {
	background-position: -11px 0;
}