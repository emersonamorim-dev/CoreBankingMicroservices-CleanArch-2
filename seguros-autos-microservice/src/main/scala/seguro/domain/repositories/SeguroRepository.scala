package seguro.domain.repositories

import seguro.domain.entities.Seguro

trait SeguroRepository {
  def salvar(seguro: Seguro): Unit
  def buscarPorVeiculo(placa: String): Option[Seguro]
  def atualizar(placa: String, seguroAtualizado: Seguro): Boolean
  def remover(placa: String): Boolean
}

// Implementação em memória
class InMemorySeguroRepository extends SeguroRepository {
  private var seguros: Map[String, Seguro] = Map()

  override def salvar(seguro: Seguro): Unit = {
    seguros += (seguro.detalhesSeguro.veiculo.placa -> seguro)
  }

  override def buscarPorVeiculo(placa: String): Option[Seguro] = {
    seguros.get(placa)
  }

  override def atualizar(placa: String, seguroAtualizado: Seguro): Boolean = {
    if (seguros.contains(placa)) {
      seguros += (placa -> seguroAtualizado)
      true
    } else {
      false 
    }
  }

  override def remover(placa: String): Boolean = {
    if (seguros.contains(placa)) {
      seguros -= placa
      true
    } else {
      false 
    }
  }
}
