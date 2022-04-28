function deleteEvent(id, name) {
    document.getElementById('modalName').innerHTML = name;
    document.getElementById('modalConfirm').setAttribute("href", "/eventos/deletar-evento/" + id);
}

function deleteGuest(guestId, guestName) {
    option = confirm("Deseja excluir o convidado [" + guestName + "]?");
    if (option) {
        alert("Convidado excluído com sucesso!");
        window.location.replace("/eventos/deletar-convidado/" + guestId);
    }
}