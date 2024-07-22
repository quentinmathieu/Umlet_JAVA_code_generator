package fr.afpa.uxftojava;

class Relation {
	private String lt;
	private String m1 = null;
	private String m2 = null;
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
	public Relation(String coordinatesString, String widthHeightString, UxfParser uxfParser, String arrow) {
		this.uxfParser = uxfParser;
		this.parseCoordinates(coordinatesString);
		this.parseWidthHeight(widthHeightString);
		this.parseArrow(arrow);
		this.uxfParser.addRelation(this);
	}

	// Get arrow infos : get the arrow type and the cardinality if there is one
	public boolean parseArrow(String arrow){
		if (arrow.contains("m1=") && arrow.contains("m2=")){
			this.m1= arrow.split("\n")[1].split("\\..")[1];
			this.m2= arrow.split("\n")[2].split("\\..")[1];
		}
		this.lt = arrow.split("\n")[0].split("\\=")[1];
		return true;
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

	public boolean linkObjects(){
		// set objects' extends & foreign attributes of the relation
		if(!this.lt.contains("<") && !this.lt.contains(">")){
			switch (m1) {
				case "1":
					if("n".equals(m2)){
						System.out.println(startClass.getName() + " 1.n " + endClass.getName());
					}
					break;
			
				case "n":
					if("1".equals(m2)){
						System.out.println(startClass.getName() + " n.1 " + endClass.getName());
					}
					else if ("n".equals(m2)){
						System.out.println(startClass.getName() + " n.n " + endClass.getName());
					}
					break;
				
				default:
					break;
			}
		}
		return true;
	}

	public Integer calcEndXPos() {
		return this.getWidth() + this.getXPos();
	}

	public Integer calcEndYPos() {
		return this.getHeight() + this.getYPos();
	}

	// --------------getters & setters--------------\\


	public String getLt() {
		return this.lt;
	}

	public void setLt(String lt) {
		this.lt = lt;
	}

	public String getM1() {
		return this.m1;
	}

	public void setM1(String m1) {
		this.m1 = m1;
	}

	public String getM2() {
		return this.m2;
	}

	public void setM2(String m2) {
		this.m2 = m2;
	}

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
