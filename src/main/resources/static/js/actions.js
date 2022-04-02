// shorturl.at/hqBKX

function deleteEvent(id, name) {
    option = confirm("Deseja excluir o evento [" + name + "]?");
    if (option) {
        $.ajax({
            type: "GET",
            url: "/deletarEvento",
            async: false,
            data: "codigo=" + id,
            success: function () {
                alert("Evento excluído com sucesso!");
                location.replace("/");
            }
        });
    }
}

function deleteGuest(guestId, guestName, eventId) {
    option = confirm("Deseja excluir o convidado [" + guestName + "]?");
    if (option) {
        $.ajax({
            type: "GET",
            url: "/deletarConvidado",
            async: false,
            data: "rg=" + guestId,
            success: function () {
                alert("Convidado excluído com sucesso!");
                location.replace("/" + eventId);
            }
        });
    }
}