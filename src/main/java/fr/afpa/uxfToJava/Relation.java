package fr.afpa.uxftojava;

class Relation{
    private String arrow;
	private Integer xPos;
	private Integer yPos;
	private Integer width;
	private Integer height;
	private Class fromClass;
	private Class toClass;
	private UxfParser uxfParser;

	//--------------construct--------------\\
	public Relation (String coordinatesString, String widthHeightString, UxfParser uxfParser){
        this.uxfParser = uxfParser;
		this.parseCoordinates(coordinatesString);
		this.parseWidthHeight(widthHeightString);
        this.uxfParser.addRelation(this);
	}

	// parse coordinate from string to to x / y / width / height
    public boolean parseCoordinates(String coordinatesString){
        String[] localCoordinates = coordinatesString.replace(" ", "").split("\n");

		this.xPos = Integer.parseInt(localCoordinates[1]);
		this.yPos = Integer.parseInt(localCoordinates[2]);

        return true;
    }
    
    public boolean parseWidthHeight(String widthHeightString){
        String[] localWidthHeight = widthHeightString.replace(" ", "").split(";");

		
        // arrow coordinates : x1;y1;x2;y2 (relative to the xPos yPos) to width a height 
		this.width = Math.round(Float.parseFloat(localWidthHeight[2]))-Math.round(Float.parseFloat(localWidthHeight[0]))+20;
		this.height = Math.round(Float.parseFloat(localWidthHeight[3]))-Math.round(Float.parseFloat(localWidthHeight[1]))+20;
		this.width = (int)((double)this.width*(uxfParser.getZoom()));
		this.height = (int)((double)this.height*(uxfParser.getZoom()));
        return true;
    }

	//--------------getters & setters--------------\\
	public Integer getYPos(){
		 return this.yPos;
	}

	public void setYPos (Integer yPos){
		this.yPos = yPos;
	}

	public String getArrow(){
		 return this.arrow;
	}

	public void setArrow (String arrow){
		this.arrow = arrow;
	}

	public Integer getWidth(){
		 return this.width;
	}

	public void setWidth (Integer width){
		this.width = width;
	}

	public Integer getXPos(){
		 return this.xPos;
	}

	public void setXPos (Integer xPos){
		this.xPos = xPos;
	}

	public Integer getHeight(){
		 return this.height;
	}

	public void setHeight (Integer height){
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
