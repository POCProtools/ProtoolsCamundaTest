# POC de Protools avec Camunda Platform7
Exécution du protocole d'enquête minimal avec Camunda Platform 7 (ex community edition)
### Installation
[Lien vers le deployment](https://protools-camunda.dev.insee.io/camunda/app/welcome/default/#!/login)

##### Installation via Docker
```bash
docker pull mailinenguyen/protools-camunda
docker run -d --name protoolscamunda -p 8080:8080  mailinenguyen/protools-camunda:latest
```
##### Installation manuelle
``` bash
git clone git@github.com:Stage2022/ProtoolsCamundaTest.git
cd ProtoolsCamundaTest
./mvnw spring-boot:run
```

#### TODO:
- Tests
