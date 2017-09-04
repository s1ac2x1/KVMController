# Testing as a Service #

Simple mock for testing environment using KVM.

### The idea ###

Main controller must be able to receive start / stop / status commands over an API and use KVM to start a VM, request its status and stop it.

### Requirements ###

* The controller provides an API to control VMs
* The controller runs on a linux host
* The VM is based on a Windows base image
* REST path should be flexible, given the defaults: /api/vm/{command}/<mac_address>