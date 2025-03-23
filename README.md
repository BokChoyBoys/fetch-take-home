# Fetch Take Home

## How to run
``docker build -t fetch .`` \
then \
``docker run -p 8080:8080 fetch``

## Endpoints

POST ``http://localhost:8080/receipts/process`` \
GET ``http://localhost:8080/receipts/{id}/points``
