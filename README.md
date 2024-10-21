# Umlet JAVA code generator
This is a simple tool to generate code from an UML diagram created with [Umlet](https://github.com/umlet/umlet).

To test this app you can find the uxf samples in the [uxfForTest](uxfForTest/) folder

This tool generate java class files according to the source UML :
* All attibutes (with their types) of each class (including attributes generated from relations)
* Getters & setters
* Constructs


## 🛠️ Installation Steps:

### Prerequisites :
* Install the latest version of [Java](https://java.com/) and [Maven](https://maven.apache.org/download.html).

<!-- ### As a simple app -->

### Steps :
1. Clone this repo

2. In the [Main.java](src/main/java/fr/afpa/uxftojava/Main.java) file replace the file by your uxf file (you have some sample in the [uxfForTest](uxfForTest/) folder):

    ```java
    public static void main(String[] args)
    {
    //...
    new UxfParser("pathTo/yourFile.uxf");
    }
    ```
3. Compile the app :
    ```bash
    mvn compile
    ```
4. Run the app :
    ```bash
    mvn -q exec:java
    ```

<!-- ### As a dependency -->
