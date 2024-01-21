# Env setup

## Database
To remove dependency on local machine setup, Docker Postgres DB is used. Version 16.1 (ref: https://hub.docker.com/_/postgres)

Note: there is no DB migration component (such as flyway) added, hence default table has to be created (`scripts/pg.sql`)

## docker pg setup:
```docker pull postgres:16.1```

```docker run --name pg16_1 -p 5432:5432 -e POSTGRES_PASSWORD=mysecretpassword -d postgres:16.1```

If there is pg container with a given name remove it to start from scratch: `docker rm pg16_1` and then `docker start pg16_1`

# Step 1 - Playframework - naive CR(UD) app

## start webapp
`sbt clean run`

## API
### get all Items
`curl localhost:9000/api/v1/getall`

### add new Item
```curl -X POST localhost:9000/api/v1/addnew -H 'Content-Type: application/json' -d '{"name":"new_item","flag":true}'```

# Step 2 - Playframework with Tapir
## API
### get all Items
`curl localhost:9000/api/v2/getall`

### add new Item
```curl -X POST localhost:9000/api/v2/addnew -H 'Content-Type: application/json' -d '{"name":"new_item","flag":true}'```

# Step 3 - Tagless-ish Final applied to business logic

The only change is to decorate traits with F[_]

# Step 4 - Http4s pushing out Playframework

`api` folder from Playframework to delete and move business logic to regular `src` dir.
