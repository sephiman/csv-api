# Csv Service

This is an example of API REST service that handles CSV. The CSV contains the header the columns to map to the database. The code column is
unique.

## Dependencies:

This is the list of dependencies you will need to run the project:

- Java 8 or later.
- Gradle 6 (6.8 or later)

## How to run it:

The application will be deployed in the domain localhost through the port 8080 by default.

- Run application using Gradle under project directory

`gradle bootRun`

## Additional configuration:

Use the following properties to customize the application modifying _application.properties_

1. Default port. The default port may be change to any other adding the following property:

`server.port=8080`

2. H2 console. H2 with Spring boot allows to enable a web h2 client with the following properties:

`spring.h2.console.enabled=true`

`spring.h2.console.path=/h2`

3. H2 allows configuring whether to use in-memory database or physical file database changing the following properties:

`spring.datasource.url=jdbc:h2:mem:testdb`

`spring.datasource.url=jdbc:h2:file:./record-database`

## Improvements

- Mapping strategy for the CSV: like parsing the dates, validating the data, making code required,...
- Proper exception management with advice controller
- Integration tests
- Unit tests for mappers

## Endpoints

The record API provides the following endpoints:

### Fetch all data

Retrieves a list of all existing records in database

- Method: GET
- URI: /api/records
- Content-Type: \*/\*

Possible responses are:

- Http 200 Ok: It returns the existing items

**Example cURL request**

    curl --location --request GET 'http://localhost:8080/api/records'

**Example response**

    HTTP/1.1 200 
    Content-Disposition: attachment; filename=ZTpcfbgb7cENdAtHqFJvvsQcw5jvy5ju16872966139565585786.csv
    Accept-Ranges: bytes
    Content-Type: text/csv
    Content-Length: 1571
    Date: Sat, 10 Jul 2021 10:30:55 GMT
    
    "CODE","CODELISTCODE","DISPLAYVALUE","FROMDATE","LONGDESCRIPTION","SORTINGPRIORITY","SOURCE","TODATE"
    "271636001","ZIB001","Polsslag regelmatig","01-01-2019","The long description is necessary","1","ZIB",""
    "61086009","ZIB001","Polsslag onregelmatig","01-01-2019","","2","ZIB",""
    "Type 1","ZIB001","Losse harde keutels, zoals noten","01-01-2019","","","ZIB",""
    "Type 2","ZIB002","Als een worst, maar klonterig","01-01-2019","","","ZIB",""
    "Type 3","ZIB002","Als een worst, maar met barstjes aan de buitenkant","01-01-2019","","","ZIB",""
    "Type 4","ZIB002","Als een worst of slang, glad en zacht","01-01-2019","","","ZIB",""
    "Type 5","ZIB002","Zachte keutels met duidelijke randen","01-01-2019","","","ZIB",""
    "Type 6","ZIB002","Zachte stukjes met gehavende randen","01-01-2019","","","ZIB",""
    "Type 7","ZIB002","Helemaal vloeibaar","01-01-2019","","","ZIB",""
    "307047009","ZIB003","Rectale temperatuur","01-01-2019","","1","ZIB",""
    "415945006","ZIB003","Orale temperatuur (onder de tong)","01-01-2019","","2","ZIB",""
    "415882003","ZIB003","Axillaire temperatuur (onder de oksel)","01-01-2019","","","ZIB",""
    "415929009","ZIB003","Inguinale temperatuur (via de lies)","01-01-2019","","","ZIB",""
    "415974002","ZIB003","Tympanische temperatuur","01-01-2019","","3","ZIB",""
    "415922000","ZIB003","Temporale temperatuur","01-01-2019","","","ZIB",""
    "364246006","ZIB003","Vaginale temperatuur","01-01-2019","","","ZIB",""
    "698832009","ZIB003","Blaastemperatuur","01-01-2019","","","ZIB",""
    "276885007","ZIB003","Kern temperatuur (invasief gemeten)","01-01-2019","","","ZIB",""

### Fetch by code

Retrieves one single record if exists providing a code

- Method: GET
- URI: /api/records/{code}
- Content-Type: \*/\*

Possible responses are:

- Http 200 Ok: The object has been found and returned
- Http 404 Not Found: the record has not been found

**Example cURL request**

    curl --location --request GET 'http://localhost:8080/api/records/271636001'

**Example response**

    HTTP/1.1 200 
    Content-Disposition: attachment; filename=yEqcgSrSsrffcpA9DVT4p1sBLxTRkynP8341903299869531312.csv
    Accept-Ranges: bytes
    Content-Type: text/csv
    Content-Length: 207
    Date: Sat, 10 Jul 2021 10:43:04 GMT
    
    "CODE","CODELISTCODE","DISPLAYVALUE","FROMDATE","LONGDESCRIPTION","SORTINGPRIORITY","SOURCE","TODATE"
    "271636001","ZIB001","Polsslag regelmatig","01-01-2019","The long description is necessary","1","ZIB",""

### Upload the data

Allows to add to database the CSV containing the records

- Method: POST
- URI: /api/records
- Content-Type: multipart/form-data;

Possible responses are:

- Http 201 Created: the object has been successfully created
- Http 400 Bad request: at least one parameter is not valid. See requirements below

#### Requirements

- CSV must contain at least the column "code"

**Example cURL request**

    curl --location --request POST 'http://localhost:8080/api/records' \
    --form 'file=@"/Users/juan/Downloads/exercise.csv"'

**Example response**

    HTTP/1.1 201 

Possible responses are:

- Http 201 Created: the records are created successfully
- Http 400 Bad request: request contains duplicated codes or existing in database codes

### Delete all data

Deletes all records in database

- Method: DELETE
- URI: /api/records
- Content-Type: */*

Possible responses are:

- Http 204 No content: all existing records are deleted

**Example cURL request**

    curl --location --request DELETE 'http://localhost:8080/api/records'

**Example response**

    HTTP 204 No Content
