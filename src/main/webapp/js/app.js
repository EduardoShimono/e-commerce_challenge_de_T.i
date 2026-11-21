document.addEventListener('DOMContentLoaded', function () {

    // Validacao dos formularios com as classes do Bootstrap
    document.querySelectorAll('form.needs-validation').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            form.querySelectorAll('[data-igual]').forEach(function (campo) {
                var outro = document.getElementById(campo.dataset.igual);
                campo.setCustomValidity(outro && outro.value !== campo.value ? 'diferente' : '');
            });

            if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Confirmacao antes de enviar (excluir produto, esvaziar carrinho)
    document.querySelectorAll('form[data-confirmar]').forEach(function (form) {
        form.addEventListener('submit', function (event) {
            if (!confirm(form.dataset.confirmar)) {
                event.preventDefault();
            }
        });
    });

    // Carrinho: atualiza a quantidade assim que o valor muda
    document.querySelectorAll('.form-quantidade input[name="quantidade"]').forEach(function (input) {
        input.addEventListener('change', function () {
            var max = parseInt(input.max, 10);
            var valor = parseInt(input.value, 10);
            if (isNaN(valor) || valor < 1) {
                input.value = 1;
            } else if (!isNaN(max) && valor > max) {
                alert('Quantidade máxima disponível em estoque: ' + max);
                input.value = max;
            }
            input.form.submit();
        });
    });

    // Checkout: mostra o detalhe da forma de pagamento escolhida
    var radios = document.querySelectorAll('input[name="formaPagamento"]');
    radios.forEach(function (radio) {
        radio.addEventListener('change', function () {
            document.querySelectorAll('.detalhe-pagamento').forEach(function (div) {
                div.classList.add('d-none');
            });
            var alvo = document.getElementById('detalhe-' + radio.value);
            if (alvo) {
                alvo.classList.remove('d-none');
            }
        });
    });

    // Mascaras simples dos campos ilustrativos do cartao
    document.querySelectorAll('[data-mascara="cartao"]').forEach(function (campo) {
        campo.addEventListener('input', function () {
            var numeros = campo.value.replace(/\D/g, '').substring(0, 16);
            campo.value = numeros.replace(/(\d{4})(?=\d)/g, '$1 ');
        });
    });
    document.querySelectorAll('[data-mascara="validade"]').forEach(function (campo) {
        campo.addEventListener('input', function () {
            var numeros = campo.value.replace(/\D/g, '').substring(0, 4);
            campo.value = numeros.length > 2 ? numeros.substring(0, 2) + '/' + numeros.substring(2) : numeros;
        });
    });
});
