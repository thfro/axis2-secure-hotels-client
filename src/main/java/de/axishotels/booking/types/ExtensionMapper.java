/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.9  Built on : Nov 16, 2018 (12:06:07 GMT)
 */
package de.axishotels.booking.types;


/**
 *  ExtensionMapper class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ExtensionMapper {
    public static java.lang.Object getTypeObject(
        java.lang.String namespaceURI, java.lang.String typeName,
        javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
        if ("http://axishotels.de/booking/types".equals(namespaceURI) &&
                "Confirmation".equals(typeName)) {
            return de.axishotels.booking.types.Confirmation.Factory.parse(reader);
        }

        if ("http://axishotels.de/booking/types".equals(namespaceURI) &&
                "Hotel".equals(typeName)) {
            return de.axishotels.booking.types.Hotel.Factory.parse(reader);
        }

        if ("http://axishotels.de/booking/types".equals(namespaceURI) &&
                "Reservation".equals(typeName)) {
            return de.axishotels.booking.types.Reservation.Factory.parse(reader);
        }

        if ("http://axishotels.de/booking/types".equals(namespaceURI) &&
                "RoomType".equals(typeName)) {
            return de.axishotels.booking.types.RoomType.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}
