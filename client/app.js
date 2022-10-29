var socket = new SockJS('http://localhost:8080/ws');

const btn = document.getElementById("search");
const results = document.getElementById("results");


btn.addEventListener("click", (e) => {

    const shop = document.getElementById("shop");
    const terms = document.getElementById("terms");

    const shopValue = shop.value
    const termsValue = terms.value.split(";").filter(el => el != undefined);
    stompClient.send("/app/links.start", {}, JSON.stringify({ shop: shopValue, terms: termsValue }));
});

function connect() {
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/link.finish', function (search) {
            console.log(search.body)
        });
        stompClient.subscribe('/topic/link.stats.count', function (count) {
            console.log(count.body)
        });
        stompClient.subscribe('/topic/link.stats.term', function (term) {
            console.log(term.body)
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

window.addEventListener('load', (event) => {
    connect();
});