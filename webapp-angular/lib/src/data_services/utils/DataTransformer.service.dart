/*
 * ******************************************************************************
 *  * Copyright (c) 2017 Arthur Deschamps
 *  *
 *  * All rights reserved. This program and the accompanying materials
 *  * are made available under the terms of the Eclipse Public License v1.0
 *  * which accompanies this distribution, and is available at
 *  * http://www.eclipse.org/legal/epl-v10.html
 *  *
 *  * Contributors:
 *  *     Arthur Deschamps
 *  ******************************************************************************
 */

import 'dart:async';
import 'dart:convert';
import 'package:angular2/angular2.dart';
import 'package:logging/logging.dart';
import 'package:webapp_angular/src/data_services/app/company/Address.dart';
import 'package:webapp_angular/src/data_services/app/company/Coordinates.dart';
import 'package:webapp_angular/src/data_services/app/company/Customer.dart';
import 'package:webapp_angular/src/data_services/app/company/Delivery.dart';
import 'package:webapp_angular/src/data_services/app/company/Transportation.dart';
import 'package:webapp_angular/src/data_services/utils/EnumConverter.service.dart';
import 'package:webapp_angular/src/data_services/utils/Response.dart';

/// Transforms raw maps into usable objects
@Injectable()
class DataTransformerService {

  final EnumConverterService _enumConverter;
  final Logger logger = new Logger("DataTransformerService");

  DataTransformerService(this._enumConverter);

  /// Conversion to coordinates.
  Coordinates coordinates(Map rawCoordinates) {
    Map intermediateParse = JSON.decode(rawCoordinates["coordinates"]);
    return new Coordinates(intermediateParse["latitude"],intermediateParse["longitude"]);
  }

  /// Conversion to a TransportationHealthState.
  TransportationHealthState transportationHealthState(Map rawHealthState) =>
      _enumConverter.fromString(rawHealthState["health"], TransportationHealthState);

  /// Converts to a TransportationType.
  TransportationType transportationType(Map rawType) =>
      _enumConverter.fromString(rawType["transportation-type"], TransportationType);

  /// Converts to a string representing the transportation id.
  String transportationId(Map rawId) => rawId["transportation-id"];

  /// Converts to a string representing the delivery status (delivered, transit, etc).
  String deliveryStatus(Map rawStatus) => rawStatus["status"];

  /// Converts to a Delivery.
  Delivery delivery(Map rawDelivery) =>
      new Delivery(rawDelivery["id"],currentPosition: rawDelivery["currentLocation"], transporterId: rawDelivery["transporterId"]);

  /// Converts to a number (used for stores sizes, for example)
  num number(Map map) => map["number"];

  /// Converts to a string representing the name of something.
  String name(Map map) => map["name"];

  /// Converts into a string representing the business type of the company.
  String companyType(Map map) => map["company-type"];

  /// Converts to a boolean
  bool boolean(Map map) => (map["boolean"] as bool);

  /// Converts to String representing a date.
  String date(Map map) => (map["time"] as String);

  /// Converts to a list of customers.
  ///
  /// This converter is asynchronous because it requires a lot of time and resources
  /// to create customers when they are in great number.
  Future<List<Customer>> customers(Map map) async {
    List<Customer> customers = new List();
    List<Map> rawCustomers = map["customers"];
    for (final Map rawCustomer in rawCustomers) customers.add(await customer(rawCustomer));
    return customers;
  }

  Future<Customer> customer(Map rawCustomer) async => new Customer(rawCustomer["firstName"], rawCustomer["lastName"],
        address(rawCustomer), rawCustomer["emailAddress"], rawCustomer["phoneNumber"]);

  /// Converts to Address.
  Address address(Map map) {
    final Map rawAddress = map["address"];
    final Map rawCoord = rawAddress["coordinates"];
    final Coordinates coordinates = new Coordinates(rawCoord["latitude"], rawCoord["longitude"]);
    return new Address(rawAddress["street"], rawAddress["city"], rawAddress["region"],
        rawAddress["country"], rawAddress["zip"], coordinates);
  }

  /// Converts to clusters
  ///
  /// A cluster is a list of nodes (here customers)
  Future<List<List<Customer>>> customersAgglomerations(Map map) async {
      List<List<Map>> rawAgglomerations = map["customersAgglomerations"];
      List<List<Customer>> agglomerations = new List();
      for (final List<Map> rawAgglomeration in rawAgglomerations) {
        List<Customer> agglomeration = new List();
        for (final Map rawCustomer in rawAgglomeration)
          agglomeration.add(await customer(rawCustomer));
        agglomerations.add(agglomeration);
      }
      return agglomerations;
  }

  /// Converts a raw websocket message into a response object.
  Response decode(var data) {
    // Parses the response
    Map parsed = JSON.decode(data);
    Response response = new Response(parsed["topics"],parsed["data"]);
    if (response != null && response.topics != null) {
      return response;
    } else {
      logger.severe("Undecodable data :"+data);
      return null;
    }
  }
}
