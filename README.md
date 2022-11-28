# mgit - _A simulation of Git_

Create the jar file by entering the following command in the terminal<br>
`kotlinc mgit.Internals -include-runtime -d mgit.jar`<br>

### Basic functions :

1. mgit init - to initilaize the repository<br>
   `java -jar mgit.jar init`<br>

2. mgit hash-object <file-path> - to create a blob storage in the .mgit/objects folder<br>
   `java -jar mgit.jar hash-object sample.txt`<br>

3. mgit cat-file object-id - to read the contents from the blob storage<br>
   `java -jar mgit.jar cat-file 784e02ff13b3b14fd7baefc6697a7096b3c855fb`

4. mgit write-tree <path> - to create a hash object for an entire directory recursively<br>
   `java -jar mgit.jar write-tree .`

5. mgit read-tree object-id - to read thee contents that from the objects folder<br>
   `java -jar mgit.jar read-tree 784e02ff13b3b14fd7baefc6697a7096b3c855fb`
