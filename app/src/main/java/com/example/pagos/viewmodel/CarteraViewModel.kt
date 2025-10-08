package com.example.pagos.viewmodel
import com.example.pagos.repository.*
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pagos.model.*
import kotlinx.coroutines.launch
class CarteraViewModel(private val repository: PagosRepository) : ViewModel() {
    private val _carteraState = mutableStateOf<CarteraState>(CarteraState.Loading)
    val carteraState: State<CarteraState> = _carteraState

    private val _transaccionesState = mutableStateOf<List<Transaccion>>(emptyList())
    val transaccionesState: State<List<Transaccion>> = _transaccionesState

    private val _recargaState = mutableStateOf<RecargaState>(RecargaState.Idle)
    val recargaState: State<RecargaState> = _recargaState

    private val _pagoState = mutableStateOf<PagoState>(PagoState.Idle)
    val pagoState: State<PagoState> = _pagoState

    fun loadCartera(userId: Long) {
        viewModelScope.launch {
            try {
                val cartera = repository.getCartera(userId)
                _carteraState.value = CarteraState.Success(cartera)
            } catch (e: Exception) {
                _carteraState.value = CarteraState.Error(e.message ?: "Error al cargar cartera")
            }
        }
    }

    fun loadTransacciones() {
        viewModelScope.launch {
            try {
                val transacciones = repository.getHistorialTransacciones()
                _transaccionesState.value = transacciones
            } catch (e: Exception) {
                // Manejar error
            }
        }
    }

    fun realizarRecarga(monto: Double, descripcion: String, userId: Long) {
        viewModelScope.launch {
            _recargaState.value = RecargaState.Loading
            try {
                // Crear transacción de recarga
                val transaccion = Transaccion(
                    idTransaccion = 0,
                    cartera = Cartera(idCartera = 0, alumno = Usuario(userId, "", "", "", "", true), saldo = 0.0, fechaActualizacion = ""),
                    tipo = "RECARGA",
                    monto = monto,
                    fecha = "",
                    descripcion = descripcion,
                    realizadoPor = Usuario(userId, "", "", "", "", true)
                )
                repository.realizarRecarga(transaccion)
                _recargaState.value = RecargaState.Success
                loadCartera(userId) // Recargar datos
            } catch (e: Exception) {
                _recargaState.value = RecargaState.Error(e.message ?: "Error en recarga")
            }
        }
    }

    fun realizarPago(carnetBeneficiario: String, idPagador: Long, monto: Double, descripcion: String) {
        viewModelScope.launch {
            _pagoState.value = PagoState.Loading
            try {
                val paymentRequest = PaymentRequest(carnetBeneficiario, idPagador, monto, descripcion)
                val success = repository.realizarPago(paymentRequest)
                if (success) {
                    _pagoState.value = PagoState.Success
                    loadCartera(idPagador)
                } else {
                    _pagoState.value = PagoState.Error("Error en el pago")
                }
            } catch (e: Exception) {
                _pagoState.value = PagoState.Error(e.message ?: "Error en pago")
            }
        }
    }
}

sealed class CarteraState {
    object Loading : CarteraState()
    data class Success(val cartera: Cartera) : CarteraState()
    data class Error(val message: String) : CarteraState()
}

sealed class RecargaState {
    object Idle : RecargaState()
    object Loading : RecargaState()
    object Success : RecargaState()
    data class Error(val message: String) : RecargaState()
}

sealed class PagoState {
    object Idle : PagoState()
    object Loading : PagoState()
    object Success : PagoState()
    data class Error(val message: String) : PagoState()
}