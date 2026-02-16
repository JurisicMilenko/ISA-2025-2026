const amqp = require('amqplib');
const protobuf = require('protobufjs');

const QUEUE_NAME = 'UploadVideoQueue';
const MESSAGES = 250;

async function benchmark() {
    const connection = await amqp.connect('amqp://guest:guest@localhost:5672');
    const channel = await connection.createChannel();
    await channel.assertQueue(QUEUE_NAME, { durable: true });

    const root = await protobuf.load("upload_event.proto");
    const UploadEvent = root.lookupType("projekat.ISA.Proto.UploadEvent");

    let jsonSerTimes = [];
    let jsonDeserTimes = [];
    let jsonSizes = [];

    let protoSerTimes = [];
    let protoDeserTimes = [];
    let protoSizes = [];

    console.log(`Sending and measuring ${MESSAGES} messages...`);

    for (let i = 0; i < MESSAGES; i++) {
        const sample = {
            title: `Video ${i}`,
            author: `Author ${i}`,
            description: `Description ${i}`,
            uploadTimestamp: { seconds: Math.floor(Date.now() / 1000), nanos: 0 }
        };

        //JSON
        let start = process.hrtime.bigint();
        const jsonMessage = Buffer.from(JSON.stringify(sample));
        let end = process.hrtime.bigint();
        jsonSerTimes.push(Number(end - start) / 1e6); // ms

        start = process.hrtime.bigint();
        JSON.parse(jsonMessage.toString());
        end = process.hrtime.bigint();
        jsonDeserTimes.push(Number(end - start) / 1e6); // ms

        jsonSizes.push(jsonMessage.length);

        //PROTOBUF
        start = process.hrtime.bigint();
        const protoMessage = UploadEvent.encode(sample).finish();
        end = process.hrtime.bigint();
        protoSerTimes.push(Number(end - start) / 1e6); // ms

        start = process.hrtime.bigint();
        UploadEvent.decode(protoMessage);
        end = process.hrtime.bigint();
        protoDeserTimes.push(Number(end - start) / 1e6); // ms

        protoSizes.push(protoMessage.length);

        await channel.sendToQueue(QUEUE_NAME, protoMessage);
    }

    function average(arr) {
        return arr.reduce((a, b) => a + b, 0) / arr.length;
    }

    console.log("\n--json--");
    console.log(`Prosecno vreme serijalizacije: ${average(jsonSerTimes).toFixed(3)} ms`);
    console.log(`Prosecno vreme deserijalizacije: ${average(jsonDeserTimes).toFixed(3)} ms`);
    console.log(`Prosecna velicina poruke: ${average(jsonSizes)} bytes`);

    console.log("\n--protobuf--");
    console.log(`Prosecno vreme serijalizacije: ${average(protoSerTimes).toFixed(3)} ms`);
    console.log(`Prosecno vreme deserijalizacije: ${average(protoDeserTimes).toFixed(3)} ms`);
    console.log(`Prosecna velicina poruke: ${average(protoSizes)} bytes`);

    await channel.close();
    await connection.close();
}

benchmark().catch(console.error);
