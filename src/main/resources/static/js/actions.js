function deleteEvent(eventId, eventName) {
    document.getElementById("modalName").innerHTML = eventName;
    document
        .getElementById("modalConfirm")
        .setAttribute("href", "/eventos/deletar-evento/" + eventId);
}

function deleteGuest(guestId, guestName) {
    document.getElementById("modalName").innerHTML = guestName;
    document
        .getElementById("modalConfirm")
        .setAttribute("href", "/eventos/deletar-convidado/" + guestId);
}

setTimeout(function () {
    document.querySelector("div.popup").remove();
}, 4000);

document.querySelector("a.close").addEventListener("click", function () {
    document.querySelector("a.close").parentElement.remove();
});
