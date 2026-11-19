package contabancaria;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Testes unitários para a classe Conta.
 *
 * PARTE 1 — Testes de exemplo (Construtor) já estão prontos.
 *           Observe o padrão AAA e o uso de @Test e @ParameterizedTest.
 *
 * PARTE 2 — Você deve escrever os testes para os demais métodos
 *           seguindo rigorosamente o ciclo TDD: Red → Green → Refactor.
 *
 * Para cada método da classe Conta, crie testes que cubram:
 *   ✅ O cenário de sucesso (caminho feliz)
 *   ❌ Cada regra de validação (cenários de exceção)
 *   🔄 Casos de borda (valores limites)
 */
class ContaTest {

    // =======================================================
    //  PARTE 1 — EXEMPLO GUIADO: Testes do Construtor
    //  Observe o padrão Arrange-Act-Assert (AAA)
    // =======================================================

    @Test
    void construtor_DadosValidos_CriaContaCorretamente() {
        // Arrange & Act
        var conta = new Conta("Maria", 100);

        // Assert
        assertEquals("Maria", conta.getTitular());
        assertEquals(100, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_SemSaldoInicial_CriaContaComSaldoZero() {
        // Arrange & Act
        var conta = new Conta("João");

        // Assert
        assertEquals("João", conta.getTitular());
        assertEquals(0, conta.getSaldo());
        assertTrue(conta.isAtiva());
    }

    @Test
    void construtor_TitularNulo_LancaIllegalArgumentException() {
        // Assert — verifica que a exceção é lançada
        assertThrows(IllegalArgumentException.class, () -> new Conta(null));
    }

    @Test
    void construtor_TitularVazio_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta(""));
    }

    @Test
    void construtor_SaldoNegativo_LancaIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Conta("Maria", -50));
    }

    @ParameterizedTest
    @CsvSource({
        "Ana,    0",
        "Carlos, 1000",
        "Beatriz, 0.01"
    })
    void construtor_VariosValoresValidos_CriaContaCorretamente(String titular, double saldo) {
        // Act
        var conta = new Conta(titular, saldo);

        // Assert
        assertEquals(titular, conta.getTitular());
        assertEquals(saldo, conta.getSaldo(), 0.001);
        assertTrue(conta.isAtiva());
    }

    // =======================================================
    //  PARTE 2 — ESCREVA OS TESTES ABAIXO (TDD)
    //  Lembre-se: escreva o teste PRIMEIRO, veja FALHAR (Red),
    //  depois implemente o código para PASSAR (Green),
    //  e por fim faça Refactor se necessário.
    // =======================================================

    // =======================================================
    //  Testes para depositar
    //  Sugestão de testes:
    //    - Depósito com valor válido atualiza o saldo
    //    - Depósito com valor zero lança IllegalArgumentException
    //    - Depósito com valor negativo lança IllegalArgumentException
    //    - Depósito em conta inativa lança IllegalStateException
    // =======================================================
    @Test 
    void test_depositar_valido() {
        var conta = new Conta("Antonio", 100);
        conta.depositar(50);

        assertEquals(150, conta.getSaldo());
    }

    @Test
    void test_depositar_zero() {
        var conta = new Conta("Maria", 45);

        assertThrows(IllegalArgumentException.class, () -> conta.depositar(0));

        assertEquals(45, conta.getSaldo());
    }

    @Test
    void test_depositar_negativo() {
        var conta = new Conta("Maria", 45);

        assertThrows(IllegalArgumentException.class, () -> conta.depositar(-5));

        assertEquals(45, conta.getSaldo());
    }

   
    @Test 
    void test_sacar_valido() {
        var conta = new Conta("Luana", 100); 
        conta.sacar(40);

        assertEquals(60, conta.getSaldo());
    }

    @Test 
    void test_sacar_zero() {
        var conta = new Conta("Karina", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.sacar(0));

        assertEquals(100, conta.getSaldo());
    }

    @Test 
    void test_sacar_negativo() {
        var conta = new Conta("Karina", 100);

        assertThrows(IllegalArgumentException.class, () -> conta.sacar(-85));

        assertEquals(100, conta.getSaldo());
    }

    @Test 
    void sacar_acima_saldo() {
        var conta = new Conta("Marina", 100);

        assertThrows(IllegalStateException.class, () -> conta.sacar(101));

        assertEquals(100, conta.getSaldo());
    }

    @Test
    void sacar_conta_inativa() {
        var conta = new Conta("Marina", 0); 

        conta.encerrar(); // Considerando que o encerramento seja permitido

        assertThrows(IllegalStateException.class, () -> conta.sacar(40));
        assertEquals(false, conta.isAtiva());
    }

    @Test 
    void encerrar() {
        var conta = new Conta("Larissa", 0);

        conta.encerrar();

        assertEquals(false, conta.isAtiva());
    }

    @Test 
    void encerrar_saldo() {
        var conta = new Conta("Jaja", 56);

        assertThrows(IllegalStateException.class, () -> conta.encerrar());

        assertEquals(true, conta.isAtiva());
        assertEquals(56, conta.getSaldo());
    }

    @Test 
    void encerrar_duas() {
        var conta = new Conta("Larissa", 0);

        conta.encerrar();

        assertEquals(false, conta.isAtiva());

        assertThrows(IllegalStateException.class, () -> conta.encerrar());

        assertEquals(false, conta.isAtiva());
    }

    @Test 
    void encerrada_conta() {
        var conta = new Conta("Luiza", 0);
        conta.encerrar();

        assertThrows(IllegalStateException.class, () -> conta.depositar(80));

        assertEquals(false, conta.isAtiva());
    }

    @Test 
    void transferir() {
        var conta1 = new Conta("Juarez", 200);
        var conta2 = new Conta("Kila", 100);

        conta1.transferir(conta2, 50);

        assertEquals(150, conta1.getSaldo());
        assertEquals(150, conta2.getSaldo());
    }

    @Test
    void transferirZero() {
        var conta1 = new Conta("Juarez", 200);
        var conta2 = new Conta("Kila", 100);

        assertThrows(IllegalArgumentException.class, () -> conta1.transferir(conta2, 0));

        assertEquals(200, conta1.getSaldo());
        assertEquals(100, conta2.getSaldo());
    }

    @Test
    void transferirNegativo() {
        var conta1 = new Conta("Juarez", 200);
        var conta2 = new Conta("Kila", 100);

        assertThrows(IllegalArgumentException.class, () -> conta1.transferir(conta2, -50));

        assertEquals(200, conta1.getSaldo());
        assertEquals(100, conta2.getSaldo());
    }

    @Test
    void transferirAcimaDoSaldo() {
        var conta1 = new Conta("Juarez", 200);
        var conta2 = new Conta("Kila", 100);

        assertThrows(IllegalStateException.class, () -> conta1.transferir(conta2, 201));

        assertEquals(200, conta1.getSaldo());
        assertEquals(100, conta2.getSaldo());
    }

    @Test
    void transferirContaOrigemInativa() {
        var conta1 = new Conta("Juarez", 0);
        var conta2 = new Conta("Kila", 100);

        conta1.encerrar();

        assertThrows(IllegalStateException.class, () -> conta1.transferir(conta2, 50));

        assertEquals(0, conta1.getSaldo());
        assertEquals(100, conta2.getSaldo());
    }

    @Test
    void transferirContaDestinoInativa() {
        var conta1 = new Conta("Juarez", 200);
        var conta2 = new Conta("Kila", 0);

        conta2.encerrar();

        assertThrows(IllegalStateException.class, () -> conta1.transferir(conta2, 50));

        assertEquals(200, conta1.getSaldo());
        assertEquals(0, conta2.getSaldo());
    }
}
