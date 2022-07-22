var stomp = null;

window.onload = function() {
    connect();
};

function connect() {
    var socket = new SockJS('/websocket');
    stomp = Stomp.over(socket);
    stomp.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stomp.subscribe('/topic/products', function (product) {
            renderItem(product);
        });
    });
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $( "#send" ).click(function() { sendContent(); });
});

function sendContent() {
    stomp.send("/app/addProductToBase", {}, JSON.stringify({
        'title': $("#title").val(),
        'price': $("#price").val()
    }));
}

function renderItem(productJson) {
    var product = JSON.parse(productJson.body);
    $("#table").append("<tr>" +
        "<td>" + product.title + "</td>" +
        "<td>" + product.price + "</td>" +
        "<td><a href='/products/" + product.id +"/cart'>Add to cart</a></td>" +
        "<td><a href=/products/>Delete</a></td>" +
        "</tr>");
}