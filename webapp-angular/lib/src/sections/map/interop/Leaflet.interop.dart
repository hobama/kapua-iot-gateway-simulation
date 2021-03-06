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

/// Interoperability for Leaflet JS library.
@JS()
library leafletdart;

import 'package:js/js.dart';

@JS('L')
class Leaflet {
  external static LeafletMap map(String id, MapOptions options);
  external static TileLayer tileLayer(String urlTemplate, TileLayerOptions options);
  external static LatLng latLng(num latitude, num longitude, num altitude);
  external static Marker marker(LatLng latlng, MarkerOptions options);
  external static Icon icon(IconOptions options);
  external static Circle circle(LatLng latlng, CircleOptions options);
  external static Polygon polygon(List<LatLng> latlngs, PolygonOptions options);
  external static Point point(num x, num y, bool round);
}

@JS('L.Map')
class LeafletMap {
  external String get id;
  external set id(String mapId);

  external MapOptions get mapOptions;
  external set mapOptions(MapOptions mapOptions);

  external setView(LatLng center, num zoom, ZoomPanOptions options);
  external on(String event, callback(MouseEvent e));
}

@JS('L.Layer')
class Layer {
  external addTo(LeafletMap map);
  external Layer bindPopup(dynamic content, PopupOptions options);
  external Marker setLatLng(LatLng latlng);
  external Marker remove();
  external on(String event, callback(Event e));
}

@JS()
@anonymous
class Callback {
  external String get event;
  external set event(String event);
  external Function get callback;
  external set callback(Function callback(Event e));
  external factory Callback({
    String event,
    Function callback
  });
}

@JS('L.TileLayer')
class TileLayer extends Layer {
  external String get urlTemplate;
  external set urlTemplate(String urlTemplate);

  external LayerOptions get options;
  external set tileLayerOptions(LayerOptions options);

}

@JS("L.LatLng")
class LatLng {
  external num get latitude;
  external set latitude(num latitude);

  external num get longitude;
  external set longitude(num longitude);

  external num get altitude;
  external set altitude(num altitude);
}

@JS('L.Icon')
class Icon {
  external String get iconUrl;
  external set iconUrl(String iconUrl);

  external NumPair get iconSize;
  external set iconSize(NumPair iconSize);

  external NumPair get iconAnchor;
  external set iconAnchor(NumPair iconAnchor);

  external NumPair get popupAnchor;
  external set popupAnchor(NumPair popupAnchor);

  external String get shadowUrl;
  external set shadowUrl(String shadowUrl);

  external NumPair get shadowSize;
  external set shadowSize(NumPair shadowSize);

  external NumPair get shadowAnchor;
  external set shadowAnchor(NumPair shadowAnchor);
}

@JS('L.Marker')
class Marker extends Layer {
  external LatLng get latlng;
  external set latlng(LatLng latlng);

  external MarkerOptions get options;
  external set options(MarkerOptions options);

  external on(String event, callback(MouseEvent e));
}

@JS('L.circle')
class Circle extends Layer {
    external setRadius(num radius);
    external num getRadius();
    external num getBounds();
}

@JS('L.polygon')
class Polygon extends Layer {

}

@JS('L.point')
class Point {
  external num get x;
  external set x(num x);
  external num get y;
  external set y(num y);
  external bool get round;
  external set round(bool round);
}

@JS()
@anonymous
class Event {
  external String get type;
  external set type(String type);
  external Object get target;
  external set target(Object target);
}

@JS()
@anonymous
class MouseEvent extends Event {
  external LatLng get latlng;
  external set latlng(LatLng latlng);
  external Point get layerPoint;
  external set layerPoint(Point layerPoint);
  external Point get containerPoint;
  external set containerPoint(Point layerPoint);
}

@JS()
@anonymous
class LayerOptions {
  external String get pane;
  external set pane(String pane);

  external String get attribution;
  external set attribution(String attribution);

  external factory LayerOptions({
    String pane,
    String attribution
  });
}

@JS()
@anonymous
class PolygonOptions extends LayerOptions {
  external num get smoothFactor;
  external set smoothFactor(num smoothFactor);

  external bool get noClip;
  external set noClip(bool noClip);

  external factory PolygonOptions({
    num smoothFactor,
    bool noClip
  });
}

@JS()
@anonymous
class CircleOptions extends LayerOptions {
  external num get radius;
  external set radius(num radius);
  external factory CircleOptions({
    num radius
  });
}

@JS()
@anonymous
class PopupOptions extends LayerOptions {
  external factory PopupOptions({
    num maxWidth,
    num minWidth
  });
}

@JS()
@anonymous
class NumPair {
  external num get first;
  external set first(num first);

  external num get second;
  external set second(num second);
}

@JS()
@anonymous
class MarkerOptions extends LayerOptions {
  external Icon get icon;
  external set icon(Icon icon);

  external bool get draggable;
  external set draggable(bool draggable);

  external String get title;
  external set title(String title);

  external num get zIndexOffset;
  external set zIndexOffset(num zIndexOffset);

  external num get opacity;
  external set opacity(num opacity);

  external bool get riseOnHover;
  external set riseOnHover(bool riseOnHover);

  external num get riseOffset;
  external set riseOffset(num riseOffset);

  external factory MarkerOptions({
    Icon icon,
    bool draggable,
    String title,
    num zIndexOffset,
    num opacity,
    bool riseOnHover,
    num riseOffset
  });
}

@JS()
@anonymous
class IconOptions {
  // TODO
}

@JS()
@anonymous
class TileLayerOptions extends LayerOptions {
    external num get minZoom;
    external set minZoom(num minZoom);

    external num get maxZoom;
    external set maxZoom(num maxZoom);

    external bool get noWrap;
    external set noWrap(bool noWrap);

    external String get attribution;
    external set attribution(String attribution);

    external factory TileLayerOptions({
      num minZoom,
      num maxZoom,
      bool noWrap,
      String attribution
    });
}

@JS()
@anonymous
class MapOptions {
  external LatLng get center;
  external set Coordinates(LatLng coordinates);

  external num get zoom;
  external set zoom(num zoom);

  external num get zoomDelta;
  external set zoomDelta(num zoomDelta);

  external factory MapOptions({
    LatLng center,
    num zoom,
    num zoomDelta
  });
}

@JS()
@anonymous
class ZoomPanOptions {
  // TODO
}