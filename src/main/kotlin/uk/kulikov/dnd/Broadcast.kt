package uk.kulikov.dnd

import java.io.IOException
import java.net.*

private const val DEST_PORT = 32411

fun main() {
    while (true) {
        println("Start round for sending")
        sendBroadcast()
        Thread.sleep(1000)
    }
}

fun sendBroadcast() {
    listAllBroadcastAddresses().forEach {
        try {
            broadcast("Hello", it)
            println("Successfull to send broadcast to $it")
        } catch (e: IOException) {
            e.printStackTrace()
            println("Failed to send broadcast to $it")
        }
    }
}

fun receiveBroadcast() {
    listAllBroadcastAddresses().forEach {
        println(it)
    }
    println("Start listening 239.255.255.250")
    receiveBroadcast(InetAddress.getByName("239.255.255.250"))

}

private fun receiveBroadcast(address: InetAddress) {
    val b = ByteArray(8 * 1024)
    val dgram = DatagramPacket(b, b.size)
    val socket = MulticastSocket(DEST_PORT) // must bind receive side
    socket.joinGroup(address)

    while (true) {
        socket.receive(dgram) // blocks until a datagram is received
        System.err.println(
            ("Received " + dgram.length).toString() +
                    " bytes from " + dgram.address
        )
        dgram.length = b.size // must reset length field!
    }
}


@Throws(IOException::class)
private fun broadcast(
    broadcastMessage: String, address: InetAddress?
) {
    val socket = DatagramSocket()
    socket.setBroadcast(true)

    val buffer = broadcastMessage.toByteArray()

    val packet = DatagramPacket(buffer, buffer.size, address, 32411)
    socket.send(packet)
    socket.close()
}

@Throws(SocketException::class)
private fun listAllBroadcastAddresses(): List<InetAddress> {
    val broadcastList: MutableList<InetAddress> = ArrayList()
    val interfaces = NetworkInterface.getNetworkInterfaces()
    while (interfaces.hasMoreElements()) {
        val networkInterface = interfaces.nextElement()

        if (networkInterface.isLoopback || !networkInterface.isUp) {
            continue
        }

        networkInterface.interfaceAddresses.stream()
            .map { a: InterfaceAddress -> a.broadcast }
            .filter { it != null }
            .forEach { e: InetAddress -> broadcastList.add(e) }
    }
    return broadcastList
}