<%@ taglib uri="http://www.zkoss.org/dsp/web/core" prefix="c" %>

.z-horbox {
	height: 300px;
}

.z-horbox-list {
	list-style: none outside none;
	margin: 0;
	padding: 0;
	height: 100%;
}

.z-horpanel-outer {
	position: relative;
	float: left;
	height: 100%;
}

.z-horpanel, .z-horpanel-tab {
	position: relative;
	float: left;
	display: inline-block;
}

.z-horpanel-tab {
	height: 100%;
	width: 30px;
}

.z-horpanel {
	height: 100%;
	display: none;
}

.z-horpanel-cnt {
	width: 300px;
}

/* debug */
.z-horpanel, .z-horpanel-tab {
	border: solid 1px #CCCCCC;
}