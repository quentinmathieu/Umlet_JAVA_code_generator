package fr.afpa.uxftojava;

class Relation {
	private String arrow;
	private Integer xPos;
	private Integer yPos;
	private Integer width;
	private Integer height;
	private Class fromClass;
	private Class toClass;
	private UxfParser uxfParser;
	private boolean invert = false;
	private Class startClass = null;
	private Class endClass = null;

	// --------------construct--------------\\
	public Relation(String coordinatesString, String widthHeightString, UxfParser uxfParser) {
		this.uxfParser = uxfParser;
		this.parseCoordinates(coordinatesString);
		this.parseWidthHeight(widthHeightString);
		this.uxfParser.addRelation(this);
	}

	// parse coordinate from string to to x / y / width / height
	public boolean parseCoordinates(String coordinatesString) {
		String[] localCoordinates = coordinatesString.replace(" ", "").split("\n");

		this.xPos = Integer.parseInt(localCoordinates[1]);
		this.yPos = Integer.parseInt(localCoordinates[2]);

		return true;
	}

	public boolean parseWidthHeight(String widthHeightString) {
		String[] localWidthHeight = widthHeightString.replace(" ", "").split(";");

		// arrow coordinates : x1;y1;x2;y2 (relative to the xPos yPos) to width a height
		this.width = Math.round(Float.parseFloat(localWidthHeight[2]))
				- Math.round(Float.parseFloat(localWidthHeight[0]));
		this.height = Math.round(Float.parseFloat(localWidthHeight[3]))
				- Math.round(Float.parseFloat(localWidthHeight[1]));
		invert = (this.width < 0 || this.height < 0) ? true : false;
		this.width = (this.width < 0) ? (-this.width) + 20 : this.width + 20;
		this.height = (this.height < 0) ? (-this.height) + 20 : this.height + 20;
		this.width = (int) ((double) this.width * (uxfParser.getZoom()));
		this.height = (int) ((double) this.height * (uxfParser.getZoom()));
		return true;
	}

	public Integer calcEndXPos() {
		return this.getWidth() + this.getXPos();
	}

	public Integer calcEndYPos() {
		return this.getHeight() + this.getYPos();
	}

	// --------------getters & setters--------------\\


	public Class getStartClass() {
		return this.startClass;
	}

	public void setStartClass(Class startClass) {
		this.startClass = startClass;
	}

	public Class getEndClass() {
		return this.endClass;
	}

	public void setEndClass(Class endClass) {
		this.endClass = endClass;
	}


	public boolean isInvert() {
		return this.invert;
	}

	public boolean getInvert() {
		return this.invert;
	}

	public void setInvert(boolean invert) {
		this.invert = invert;
	}


	public Integer getYPos() {
		return this.yPos;
	}

	public void setYPos(Integer yPos) {
		this.yPos = yPos;
	}

	public String getArrow() {
		return this.arrow;
	}

	public void setArrow(String arrow) {
		this.arrow = arrow;
	}

	public Integer getWidth() {
		return this.width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getXPos() {
		return this.xPos;
	}

	public void setXPos(Integer xPos) {
		this.xPos = xPos;
	}

	public Integer getHeight() {
		return this.height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Class getFromClass() {
		return this.fromClass;
	}

	public void setFromClass(Class fromClass) {
		this.fromClass = fromClass;
	}

	public Class getToClass() {
		return this.toClass;
	}

	public void setToClass(Class toClass) {
		this.toClass = toClass;
	}

	public UxfParser getUxfParser() {
		return this.uxfParser;
	}

	public void setUxfParser(UxfParser uxfParser) {
		this.uxfParser = uxfParser;
	}

}
