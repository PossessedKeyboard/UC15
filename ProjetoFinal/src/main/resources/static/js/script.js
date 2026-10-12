// Pede confirmação antes de enviar qualquer formulário que tenha o atributo data-confirm
// (usado no botão "Apagar" da lista de livros).
document.addEventListener("submit", function (evento) {
    var mensagem = evento.target.getAttribute("data-confirm");
    if (mensagem && !window.confirm(mensagem)) {
        evento.preventDefault();
    }
});
