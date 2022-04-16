// shorturl.at/hqBKX

function deleteEvent(id, name) {
    option = confirm("Deseja excluir o evento [" + name + "]?");
    if (option){
        alert("Evento excluído com sucesso!");
        window.location.replace("/eventos/deletar-evento/" + id);
    }
}

function deleteGuest(guestId, guestName) {
    option = confirm("Deseja excluir o convidado [" + guestName + "]?");
    if (option) {
        alert("Convidado excluído com sucesso!");
        window.location.replace("/eventos/deletar-convidado/" + guestId);
    }
}