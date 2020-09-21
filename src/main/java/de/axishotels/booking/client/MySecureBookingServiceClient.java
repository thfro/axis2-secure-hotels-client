package de.axishotels.booking.client;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.axiom.om.OMXMLBuilderFactory;
import org.apache.axiom.om.impl.builder.StAXOMBuilder;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyEngine;
import org.apache.rampart.RampartMessageData;

import de.axishotels.booking.types.CreateReservationRequest;
import de.axishotels.booking.types.CreateReservationResponse;
import de.axishotels.booking.types.GetHotelsRequest;
import de.axishotels.booking.types.GetHotelsResponse;
import de.axishotels.booking.types.Hotel;
import de.axishotels.booking.types.Reservation;

public class MySecureBookingServiceClient {

	String axis2Repo = "./src/main/resources/my-client-repo/";
	String axis2xml = axis2Repo + "axis2client.xml";
	String policy = "client-policy.xml";

	public static void main(String[] args) throws Exception {
		new MySecureBookingServiceClient().run();
	}

	public void run() throws Exception {
		ConfigurationContext configContext =
				ConfigurationContextFactory.createConfigurationContextFromFileSystem(axis2Repo, axis2xml);
		
		BookingServiceStub stub = 
				new BookingServiceStub(configContext, "http://localhost:8080/axis2/services/SecureBookingService");
		configureClientWithPolicy(stub._getServiceClient(), policy);

		
		GetHotelsRequest request = new GetHotelsRequest();
		request.setCity("Hamburg");
		request.setNumberOfStars(3);

		GetHotelsResponse response = stub.getHotels(request);
		
		System.out.println("Hotel(s) returned by booking service: ");
		for (Hotel hotel : response.getHotels()) {
			System.out.println(hotel.getHotelName());
		}
		
		if (response.getHotels().length > 0) {
			Hotel hotel = response.getHotels()[0];
			String roomCode = hotel.getRoomTypes()[0].getRoomCode();

			Reservation reservation = new Reservation();
			reservation.setHotelCode(hotel.getHotelCode());
			reservation.setNumberOfRooms(1);
			reservation.setRoomCode(roomCode);
			reservation.setArrivalDate(toDate(LocalDate.of(2020, Month.SEPTEMBER, 15)));
			reservation.setDepartureDate(toDate(LocalDate.of(2020, Month.SEPTEMBER, 18)));
			
			CreateReservationRequest request2 = new CreateReservationRequest();
			request2.setReservation(reservation);
			
			CreateReservationResponse response2 = stub.createReservation(request2);
			System.out.println("Reservation number: " + response2.getConfirmation().getReservationNumber());
			System.out.println("Status: " + response2.getConfirmation().getStatus());			
		}
	}
	
	private static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
	}
	
	private void configureClientWithPolicy(ServiceClient sc, String policyPath) throws Exception {		
		// engage Rampart module
		sc.engageModule("rampart");

		// load policy
		Policy policy = loadPolicy(policyPath);

		// activate policy for service client
		Options options = sc.getOptions();
        options.setProperty(RampartMessageData.KEY_RAMPART_POLICY, policy);		
   	}

    private static Policy loadPolicy(String policyFile) throws XMLStreamException {
        InputStream is = MySecureBookingServiceClient.class.getClassLoader().getResourceAsStream(policyFile);
        XMLStreamReader xmlStreamReader =
                XMLInputFactory.newInstance().createXMLStreamReader(is);
        StAXOMBuilder builder = (StAXOMBuilder) OMXMLBuilderFactory.createStAXOMBuilder(xmlStreamReader);
        return PolicyEngine.getPolicy(builder.getDocumentElement());
    }
}