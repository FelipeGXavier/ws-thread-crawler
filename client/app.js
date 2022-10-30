var socket = new SockJS('http://localhost:8080/ws');

const btn = document.getElementById("search");
const results = document.getElementById("results");
const bar = document.getElementById("bar");
const found = document.getElementById("found");
const load = document.getElementById("load");
const addSearch = document.getElementById("add-search");
const ins = document.getElementById("ins");

let termProgressCounter = 0;
let numTerms = 0;

addSearch.addEventListener('click', (e) => {
    const input = `<div class="row element">
    <div class="col-md-6">
       <div class="form-group mb-2">
         <label for="terms">Termos:</label>
         <textarea id="terms" cols="30" rows="2" class="form-control terms"></textarea>
       </div>
     </div>
     <div class="col-md-6">
       <div class="form-group mb-1">
         <label for="shop">Loja:</label>
         <select id="shop" class="form-select shop">
           <option value="colombo">Colombo</option>
           <option value="pontofrio">Pontofrio</option>
         </select>
       </div>
     </div>
    </div>`
    ins.insertAdjacentHTML('beforeend', input);
})

btn.addEventListener("click", (e) => {
    const elements = [...document.querySelectorAll(".element")];
    const objectsSearch = elements.map(el => {
        const shop = el.querySelector("#shop");
        const terms = el.querySelector("#terms");
        const shopValue = shop.value
        const termsValue = terms.value.split(";").filter(el => el != undefined);
        return { shop: shopValue, terms: termsValue };
    });
    numTerms = objectsSearch.reduce((total, currentValue) => {
        return currentValue.terms.length + total;
    }, 0);
    bar.innerHTML = `${termProgressCounter}/${numTerms}`;
    stompClient.send("/app/links.start", {}, JSON.stringify(objectsSearch));
});

function connect() {
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/link.finish', function (search) {
            const data = JSON.parse(search.body);
            const items = data.products.map(el => {
                return `<div class="col-md-4 mb-2">
                <div class="card p-2">
                  <p class="m-0"><h5>${el.shop}</h5></p>
                  <p class="m-0"><b>${el.title}</b></p>
                  <p class="m-0"><a href="${el.url}">Acessar produto</a></p>
                  <b class="m-0">Preço: ${el.price}</b>
                  <p class="text-muted m-0">Cód Item: ${el.code}</p>
                </div>
              </div>`
            }).join("");
            load.innerHTML = `Tempo percorrido: ${data.time}s`;
            results.insertAdjacentHTML('beforeend', items);
        });
        stompClient.subscribe('/topic/link.stats.count', function (count) {
            found.innerHTML = count.body;
        });
        stompClient.subscribe('/topic/link.stats.term', function (term) {
            console.log("Chegou termo", term);
            ++termProgressCounter;
            const width = ((termProgressCounter / numTerms) * 100).toString() + "%";
            bar.style.width = width;
            bar.innerHTML = `${termProgressCounter}/${numTerms}`;
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