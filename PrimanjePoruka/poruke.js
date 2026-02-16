const amqp = require('amqplib');
const protobuf = require('protobufjs');

const QUEUE_NAME = 'UploadVideoQueue';

async function main() {
    const connection = await amqp.connect('amqp://guest:guest@localhost:5672');
    const channel = await connection.createChannel();
    await channel.assertQueue(QUEUE_NAME, { durable: true });

    const root = await protobuf.load("upload_event.proto");
    const UploadEvent = root.lookupType("projekat.ISA.Proto.UploadEvent");

    console.log("Cekam...");

    channel.consume(QUEUE_NAME, (msg) => {
        if (msg !== null) {
            const buffer = msg.content;
            try {
                const event = UploadEvent.decode(buffer);
                console.log("Protobuf:", event);
            } catch (e) {
                const jsonEvent = JSON.parse(buffer.toString());
                console.log("JSON:", jsonEvent);
            }
            channel.ack(msg);
        }
    });
}

main().catch(console.error);