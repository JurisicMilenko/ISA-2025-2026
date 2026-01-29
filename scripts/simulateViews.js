const axios = require('axios');

const replicas = [
    'http://localhost:8081',
    'http://localhost:8082'
];

const postIds = [1, 2, 3];

const totalRequests = 100;

var replicaCount = [0, 0];
var postCount = [0, 0, 0];

async function simulateViews() {
    for (let i = 0; i < totalRequests; i++) {
        const replicaIndex = Math.floor(Math.random() * replicas.length);
        const postIndex = Math.floor(Math.random() * postIds.length);
        const randomReplica = replicas[replicaIndex];
        const randomPostId = postIds[postIndex];
        try {
            await axios.get(`${randomReplica}/post/view/${randomPostId}`);
        } catch (error) {
            console.error(`Error simulating view for post ${randomPostId} on replica ${randomReplica}:`, error.message);
        }
        replicaCount[replicaIndex]++;
        postCount[postIndex]++;
    }

    console.log(`Replica 1 handled ${replicaCount[0]} requests.`);
    console.log(`Replica 2 handled ${replicaCount[1]} requests.`);
    console.log(`Post 1 was viewed ${postCount[0]} times.`);
    console.log(`Post 2 was viewed ${postCount[1]} times.`);
    console.log(`Post 3 was viewed ${postCount[2]} times.`);
}

simulateViews()