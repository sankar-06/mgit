# mgit - *A simulation of Git*

Create the jar file by entering the following command in the terminal<br>
```kotlinc Main.kt -include-runtime -d mgit.jar```<br>

### Basic functions : 
1. mgit init - to initilaize the repository<br>
```java -jar mgit.jar init```<br>

2. mgit hash-object <file-path> - to create a blob storage in the .mgit/objects folder<br>
```java -jar mgit.jar hash-object sample.txt```<br>
  
3. mgit cat-file object-id - to read the contents from the blob storage<br>
```java -jar mgit.jar cat-file 784e02ff13b3b14fd7baefc6697a7096b3c855fb```
