<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

.z-swifttab .z-swifttab-link,.z-swifttab .z-swifttab-text {
		background: url(${c:encodeURL('~./img/swifttab/tab.png')}) no-repeat scroll 0 0 transparent;
	display: block;
	padding: 0 10px;
}
li.z-swifttab {
	cursor:pointer;
}
.z-swifttab .z-swifttab-link{
	color:#27537A;
	font-weight:bold;
	line-height:1.2;
	margin:1px 0 0;
	outline:0 none;
	text-align:center;
	text-decoration:none;
	white-space:nowrap;
	background-position:100% -100px;
	display:block;
	padding:0 10px 0 0;
}
.z-swifttab-seld a.z-swifttab-link:link,
.z-swifttab-seld a.z-swifttab-link:visited,
.z-swifttab-seld a.z-swifttab-link:visited {
	cursor:text;
}

.z-swifttab-seld .z-swifttab-text,.z-swifttab a.z-swifttab-link:hover .z-swifttab-text{
	background-position:0 -50px;
}
.z-swifttab-seld .z-swifttab-link , .z-swifttab a.z-swifttab-link:hover
{
	background-position:100% -150px;
	color:#000000;
	margin-top:0;
	position:relative;
	top:1px;
	z-index:2;
}
.z-swifttab .z-swifttab-text{
	height:auto;
	width:auto;
	margin:0 0 0 1px;
	text-align:center;
	min-height:18px;
	min-width:64px;
	padding:6px 0 0 10px;
	display:block;
}