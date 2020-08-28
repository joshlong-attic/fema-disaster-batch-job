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
  Create table fema_disaster ( hash varchar(255) not null unique; fema_declaration_string varchar(255) not null) ;
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

Wait until all pods are in 'Ready' state. Then we can deploy our batch
application. 

To visit the NOTES.txt in the future:


```shell script 
$ helm get notes bootiful-batch
```

## End





