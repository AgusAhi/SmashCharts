import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smashchartss.ChatRequest
import com.example.smashchartss.ui.theme.FontTittle
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.utils.EmptyContent.headers
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.atomicfu.TraceBase.None.append
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBotScreen(navHostController: NavHostController) {
    val chatMessages = remember { mutableStateListOf<Pair<String, String>>() }
    val isLoading = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go Back", tint = Color.White)
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "ChatBot",
                            style = TextStyle(
                                fontFamily = FontTittle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            ChatBotSection(
                chatMessages = chatMessages,
                isLoading = isLoading.value,
                onSendQuery = { query ->
                    scope.launch {
                        isLoading.value = true
                        val botReply = try {
                            getAIResponse(query)
                        } catch (e: Exception) {
                            "Error: ${e.message}"
                        }
                        isLoading.value = false
                        chatMessages.add(query to botReply)
                    }
                }
            )
        }
    }
}

@Composable
fun ChatBotSection(
    chatMessages: List<Pair<String, String>>,
    isLoading: Boolean,
    onSendQuery: (String) -> Unit
) {
    val queryText = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(chatMessages) { (userMessage, botReply) ->
                Text("You: $userMessage", fontWeight = FontWeight.Bold)
                Text("Bot: $botReply", color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
            }
        }

        if (isLoading) {
            Text("Loading...", color = Color.Blue, textAlign = TextAlign.Center)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                value = queryText.value,
                onValueChange = { queryText.value = it },
                placeholder = { Text("Ask a question...") },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (queryText.value.isNotBlank()) {
                        onSendQuery(queryText.value)
                        queryText.value = ""
                    }
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Send")
            }
        }
    }
}

val httpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(Json {
            ignoreUnknownKeys = true
            isLenient = true
        })
    }
}

suspend fun getAIResponse(query: String): String {
    val apiKey = "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"
    val url = "https://api.openai.com/v1/completions"

    val request = ChatRequest(
        model = "gpt-3.5-turbo",
        prompt = query,
        max_tokens = 150
    )

    val response = httpClient.post(url) {
        headers {
            append("Authorization", "Bearer $apiKey")
        }
        contentType(ContentType.Application.Json)
        setBody(request)
    }

    return response.bodyAsText()
}
