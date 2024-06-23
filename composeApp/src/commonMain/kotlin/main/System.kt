package main

import R
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import io.github.koalaplot.core.pie.DefaultSlice
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import painterResource

private val colors = listOf(R.Color.VintageGreen, R.Color.VintageRed, R.Color.VintageBlue)

@OptIn(ExperimentalKoalaPlotApi::class)
@Composable
fun System(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.verticalScroll(rememberScrollState()).padding(16.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Text(
      text = "Distribución de la Cartera",
      style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
      modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.padding(16.dp))
    Text(
      text = "La estraegia de inversión se basa en la diversificación de la cartera de inversión en diferentes activos financieros, con el objetivo de maximizar la rentabilidad y minimizar el riesgo.",
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.padding(16.dp))
    PieChart(
      values = listOf(0.2f, 0.3f, 0.5f),
      forceCenteredPie = true,
      modifier = Modifier.fillMaxWidth(),
      labelConnector = {},
      slice = { i: Int ->
        DefaultSlice(
          color = colors[i],
          antiAlias = true,
          gap = 0.2f
        )
      },
      animationSpec = spring(1f)
    )
    Spacer(modifier = Modifier.padding(16.dp))
    Text(
      text = "Rendimiento Medio Anual : 73.80%",
      modifier = Modifier.background(color = MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp)).padding(4.dp),
      style = MaterialTheme.typography.labelLarge
    )
    Spacer(modifier = Modifier.padding(8.dp))
    Text(
      text = "Último cálculo : 01/01/2021",
      style = MaterialTheme.typography.labelLarge,
      color = Color.Gray
    )
    Spacer(modifier = Modifier.padding(8.dp))
    Column(
      verticalArrangement = spacedBy(2.dp),
    ) {
      Item("Acciones", listOf("Acciones de Empresas", "Acciones de Alto Rendimiento"))
      Item("Bonos", listOf("Bonos del Estado", "Bonos de Empresas", "Bonos de Alto Rendimiento"))
      Item("Reit", listOf("Reit de Empresas", "Reit de Alto Rendimiento"))
      Item("Efectivo", listOf("Efectivo en Cuenta", "Efectivo en Fondos"))
      Item("Materias Primas", listOf("Oro", "Plata", "Petróleo", "Gas"))
      Item("Criptomonedas", listOf("Bitcoin", "Ethereum", "Litecoin", "Ripple"))
    }
    Spacer(modifier = Modifier.padding(20.dp))
    Text(
      text = "Sistema de Inversión",
      style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
      modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.padding(16.dp))
    Text(
      text = "El rendimiento pasado no es un indicador fiable de los resultados futuros.",
      style = MaterialTheme.typography.bodyLarge
    )
  }
}

@Composable
fun Item(
  title: String,
  items: List<String>,
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(4.dp))
  ) {
    var isExpanded by remember { mutableStateOf(false) }
    Row(
      modifier = Modifier.clickable { isExpanded = !isExpanded }.fillMaxWidth().padding(16.dp)
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
        modifier = Modifier.weight(1f)
      )
      Icon(
        painter = painterResource(if (isExpanded) R.Images.ArrowUp else R.Images.ArrowDown),
        contentDescription = null,
      )
    }
    AnimatedContent(isExpanded) { expanded ->
      if (expanded) {
        Column(
          modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
          items.forEach { Text(it) }
        }
      }
    }
  }
}