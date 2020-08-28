# Bootiful Batch Jobs with Data Flow

## This Instruction Assumes

 * Java 11
 * RDBMS ( postgres / maria / mysql)
 * Kubernetes 1.18+
 * Helm 3+
 * About 45 minutes to an hour time
 
## Database Creation

The DDL for this demo's dataset is as follows:

```sql
  Create table fema_disaster (  femaDeclarationString varchar(255) not null, disasterNumber varchar(255) not null, state varchar(255) not null, declarationType varchar(255) not null, declarationDate varchar(255) not null, fyDeclared varchar(255) not null, incidentType varchar(255) not null, declarationTitle varchar(255) not null, ihProgramDeclared varchar(255) not null, iaProgramDeclared varchar(255) not null, paProgramDeclared varchar(255) not null, hmProgramDeclared varchar(255) not null, incidentBeginDate varchar(255) not null, incidentEndDate varchar(255) not null, disasterCloseoutDate varchar(255) not null, fipsStateCode varchar(255) not null, fipsCountyCode varchar(255) not null, placeCode varchar(255) not null, designatedArea varchar(255) not null, declarationRequestNumber varchar(255) not null, hash varchar(255) not null unique, lastRefresh varchar(255) not null, id varchar(255) not null );
```

## Deploy the environment with Kubernetes

Setup a namespace using 'Kubectl:
```shell script
$ kubectl create namespace bootiful-batch 
```

Add chart source for Bitnami, and install Bitnami/Spring-cloud-dataflow:

```shell script
$ helm repo add bitnami https://charts.bitnami.com/bitnami
$ helm install bootiful-batch bitnami/spring-coud-dataflow
$ watch kubectl get pods
```

Wait until all pods are in 'Ready' state, and ensure you can get into 
the Spring Cloud Dataflow console. 

visit the NOTES.txt in the future:

```shell script 
$ helm get notes bootiful-batch
```

the commands to expose dataflow console to your local machine is outlined below:

```shell script
export SERVICE_PORT=$(kubectl get --namespace default -o jsonpath="{.spec.ports[0].port}" services bootiful-batch-spring-cloud-dataflow-server)
kubectl port-forward --namespace default svc/bootiful-batch-spring-cloud-dataflow-server ${SERVICE_PORT}:${SERVICE_PORT} &
echo "http://127.0.0.1:${SERVICE_PORT}/dashboard"
```

Whereas the last command shows the URI that you'll plug into a browser tab.

## Build and Install into Dataflow

Discuss this section - add the app to SCDF, and kick it off.

## End





