# Protools-Camunda-Test
Déroulement du processus de creation d'enquête utilisant Camunda
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
- Intégration de mails (envoi)