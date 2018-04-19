This is a very simple blockchain demo implemented with Scala.

## Build the jar and run SimpleCoin node
```bash
java -jar out/artifacts/SimpleCoin_jar/SimpleCoin.jar
```

## Make transaction
```bash
curl "localhost:8080/tx" \
     -H "Content-Type: application/json" \
     -d '{"from": "sender", "to":"receiver", "amount": 3}'
```

## Mine SimpleCoin
```bash
curl localhost:8080/mine
```

## Check the blockchain
```bash
curl localhost:8080/blockchain
```
