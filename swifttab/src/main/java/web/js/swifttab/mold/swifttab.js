function (out) {
	var zcls = this.getZclass(),
		uuid = this.uuid;
	out.push('<li ', this.domAttrs_(), '>','<a class="', zcls, '-link">',
			'<span class="', zcls, '-text">','<span id="' ,uuid , '-sort" class="', zcls, '-sort">+++</span>',
				this.domContent_(), '</span></a></li>');

}