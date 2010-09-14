function (out) {
	var zcls = this.getZclass(),
		uuid = this.uuid;
	out.push('<li ', this.domAttrs_(), '>');
	out.push('<a class="', zcls, '-link">');
	out.push('<span class="', zcls, '-text">', this.domContent_(), '</span>');
	out.push('</a>');
	out.push('</li>');

}
