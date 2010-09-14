<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

.z-tab .z-tab-link,.z-tab .z-tab-text {
		background: url(${c:encodeURL('~./img/swifttab/tab.png')}) no-repeat scroll 0 0 transparent;
	display: block;
	padding: 0 10px;
}

.z-tab .z-tab-link{
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
.z-tab-seld a.z-tab-link:link,
.z-tab-seld a.z-tab-link:visited,
.z-tab-seld a.z-tab-link:visited {
	cursor:text;
}

.z-tab-seld .z-tab-text,.z-tab a.z-tab-link:hover .z-tab-text{
	background-position:0 -50px;
}
.z-tab-seld .z-tab-link , .z-tab a.z-tab-link:hover
{
	background-position:100% -150px;
	color:#000000;
	margin-top:0;
	position:relative;
	top:1px;
	z-index:2;
}
.z-tab .z-tag-link{

}

.z-tab .z-tab-text{
	height:auto;
	width:auto;
	margin:0 0 0 1px;
	text-align:center;
	min-height:18px;
	min-width:64px;
	padding:6px 0 0 10px;
	display:block;
}