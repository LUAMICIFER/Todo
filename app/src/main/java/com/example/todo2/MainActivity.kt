package com.example.todo2

import android.os.Bundle
import android.text.style.TabStopSpan
import android.util.EventLogTags.Description
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.todo2.ui.theme.Todo2Theme
import androidx.compose.foundation.layout.*
import androidx.compose.ui.viewinterop.AndroidView
import com.bumptech.glide.Glide
import android.widget.ImageView
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.delay
import com.airbnb.lottie.compose.*
//import okhttp3.internal.concurrent.Task
import com.example.todo2.Task   //Dataclass task name ke wajeh se conflict ho raha tha is liye



@Composable
fun DisplayGifFromDrawable() {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
        AndroidView(
            factory = { context ->
                ImageView(context).apply {
                    Glide.with(context)
                        .asGif()
                        .load(R.drawable.splash) // Replace with your drawable GIF name
                        .into(this)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyComposable()
        }
    }
}

@Composable
fun MyComposable() {
    var showFirstFunction by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        delay(3000L) // Delay for 3 seconds
        showFirstFunction = false
    }

    if (showFirstFunction) {
        DisplayGifFromDrawable()
    } else {
        homescreen()
    }
}//ye sirf splash screen ke liye hai
enum class Priority{High,Medium,Low}
data class Task(var Id: Int ,
                var Title : String,
                var Description : String,
                var Date : String,
                var Priority : Priority,
                var isCompleted : MutableState<Boolean> = mutableStateOf(false))

var TaskIdCounter=0;
@Composable
fun LottieAnimationExample() {
    // Load the Lottie animation file
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.addanimation))
    // Create an animation state
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Set the number of loops
    )

    // Display the animation
    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.fillMaxSize()
    )

}
@Composable
fun Lottie2() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation2))
    // Create an animation state
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever // Set the number of loops
    )

    // Display the animation
    LottieAnimation(
        composition = composition,
        progress = progress,
        modifier = Modifier.fillMaxSize()
    )
}


@Composable
fun homescreen(){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFF9ABCC4))){
        val TaskList = remember { mutableStateListOf<Task>() }//list of dataclass Task main yahi hia isko hi filter karenge
        var isPressed by remember { mutableStateOf(false) }
        val highPriorityTasks = TaskList.filter { it.Priority == Priority.High }.filterNot {it.isCompleted.value }
        val mediumPriorityTasks = TaskList.filter { it.Priority == Priority.Medium }.filterNot {it.isCompleted.value }
        val lowPriorityTasks = TaskList.filter { it.Priority == Priority.Low }.filterNot {it.isCompleted.value }
        val completed= TaskList.filter { it.isCompleted.value }.filterNot {!it.isCompleted.value }
        var ptExpandable by remember { mutableStateOf(true) }
        var ctExpandable by remember { mutableStateOf(true) }
        var isEditing by remember { mutableStateOf(false) }
        var editingTaskId by remember { mutableStateOf(-1) }
        val pendingtask = highPriorityTasks.size + mediumPriorityTasks.size + lowPriorityTasks.size

        val scale by animateFloatAsState(
            targetValue = if (isPressed) 0.8f else 1f,
            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .size(100.dp)
                    .clickable { isPressed = !isPressed }) {
                    LottieAnimationExample()
            }
//            IconButton(onClick = {isPressed=!isPressed}) {
//                Icon(Icons.Default.Add,modifier= Modifier
//                    .size(150.dp)
//                    .scale(scale) ,contentDescription = "create")
//            }
//            LazyListView(highPriorityTasks)
//            LazyListView(mediumPriorityTasks)
//            LazyListView(lowPriorityTasks)

            Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceBetween) {
                Box(
                ) {
                    Button(
                        onClick = {ptExpandable=true} ,
                        modifier = Modifier
                            .width(150.dp)
                            .border(
                                width = 2.dp, //width of the border
                                color = Color(0xFFe5e5e5).copy(alpha = 0.6f),           // Color of the border
                                shape = RoundedCornerShape(12.dp) // Shape of the border
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF363636).copy(alpha=0.2f),
                            contentColor = Color(0xFFCED2D3)
                        )
                    ) {
                    Text(text = "Pending Task \n$pendingtask")}//pending task count
                }

                Box(
                ) {
                    Button(
                        onClick = {ctExpandable=true},
                        modifier = Modifier
                            .width(150.dp)
                            .border(
                                width = 2.dp, //width of the border
                                color = Color(0xFFe5e5e5).copy(alpha = 0.6f),           // Color of the border
                                shape = RoundedCornerShape(12.dp) // Shape of the border
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF363636).copy(alpha=0.2f),
                            contentColor = Color(0xFFCED2D3)
                        )
                    ){
                    Text(text = "Completed Task \n ${completed.size}")}
                }
            }


            Card( //ye pending task wala expandable card hai
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    .padding(8.dp)
                    .clickable { ptExpandable = !ptExpandable },
                elevation = CardDefaults.cardElevation(4.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5A636A))
                        .verticalScroll(rememberScrollState())
                        .background(MaterialTheme.colorScheme.inversePrimary)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Pending Task")
                    }
                }
                if (isEditing) { //ye edit wla dailog box hai
                    EditList(
                        TaskList = TaskList,
                        taskId = editingTaskId,
                        onCancel = { isEditing = false } // Exit edit mode on cancel
                    )
                }else{
                    if (ptExpandable) {
                        Spacer(Modifier.height(8.dp))

                        LazyListView(
                            taskList = highPriorityTasks,
                            onTaskCompletedChange = { task, isCompleted ->
                                task.isCompleted.value = isCompleted
                            },
                            onDelete = { taskId ->
                                TaskList.removeIf { it.Id == taskId }
                            },
                            onEdit = { taskId ->
                                isEditing = true // Set editing mode to true
                                editingTaskId = taskId
                            })
                        LazyListView(
                            taskList = mediumPriorityTasks,
                            onTaskCompletedChange = { task, isCompleted ->
                                task.isCompleted.value = isCompleted
                            },
                            onDelete = { taskId ->
                                TaskList.removeIf { it.Id == taskId }
                            },
                            onEdit = { taskId ->
                                isEditing = true // Set editing mode to true
                                editingTaskId = taskId
                            })
                        LazyListView(
                            taskList = lowPriorityTasks,
                            onTaskCompletedChange = { task, isCompleted ->
                                task.isCompleted.value = isCompleted
                            },
                            onDelete = { taskId ->
                                TaskList.removeIf { it.Id == taskId }
                            },
                            onEdit = { taskId ->
                                isEditing = true // Set editing mode to true
                                editingTaskId = taskId
                            })
                    }
                }
            }

            //ye completed task wala expandable card hai
            Card(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    .padding(8.dp)
                    .clickable { ctExpandable = !ctExpandable },
                elevation = CardDefaults.cardElevation(4.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5A636A))
                        .background(MaterialTheme.colorScheme.inversePrimary)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "completed Task")

                    }
                }
                if (ctExpandable) {
                    Spacer(Modifier.height(8.dp))
                    CompletedLazyListView(completed) { task, isCompleted ->
                        task.isCompleted.value = isCompleted
                    }
                }


            }
//             Background Image
            if(pendingtask==0) {
                Lottie2()
            }

        }
        if (isPressed) {
            Column(modifier=Modifier.fillMaxWidth(),horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(200.dp))
            DailogBox(onCancel = {isPressed = false},TaskList)}
        }

    }
}
@Composable
fun EditList(
    TaskList: SnapshotStateList<Task>,
    taskId: Int,
    onCancel: () -> Unit
) {
    // Find the task safely
    val etask = TaskList.find { it.Id == taskId }
    if (etask == null) {
        Text("Task not found!")
        return
    }

    // Local state for editing
    var Title by remember { mutableStateOf(etask.Title) }
    var description by remember { mutableStateOf(etask.Description) }
    var date by remember { mutableStateOf(etask.Date) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Editable text fields
        OutlinedTextField(
            value = Title,
            onValueChange = { Title = it },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Description") }
        )
        OutlinedTextField(
            value = date,
            onValueChange = { date = it },
            label = { Text("Date") }
        )

        // Action buttons
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(onClick = {
                val index = TaskList.indexOfFirst { it.Id == etask.Id }
                if (index != -1) {
                    // Update the task directly in the list
                    TaskList[index] = TaskList[index].copy(
                        Title = Title,
                        Description = description,
                        Date = date
                    )
                }
                onCancel()
            }) { Text(text = "save") }
            Button(onClick = {onCancel()}) { Text(text = "cancel") }
        }
    }
}

@Composable
fun LazyListView(taskList: List<Task>, onTaskCompletedChange: (Task, Boolean) -> Unit,onDelete: (Int) -> Unit,onEdit: (Int) -> Unit) {

    LazyColumn(modifier = Modifier.wrapContentSize()) {
        items(taskList) { ttask ->
            var Expandable by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    .padding(8.dp)
                    .clickable { Expandable = !Expandable },
                elevation = CardDefaults.cardElevation(4.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF5A636A))
                        .background(MaterialTheme.colorScheme.inversePrimary)
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(modifier = Modifier.weight(1f),
                            checked = ttask.isCompleted.value,
                            onCheckedChange = { isChecked ->
                                ttask.isCompleted.value = isChecked
                            }
                        )


                        Text(modifier = Modifier.weight(7f),
                                text = ttask.Title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                        )
                        Text(modifier = Modifier.weight(2f),
                            text = ttask.Priority.name,
                            color = when (ttask.Priority) {
                                Priority.High -> Color(0xFFFFFFFF)
                                Priority.Medium -> Color(0xFF979797)
                                Priority.Low -> Color(0xFF121212)
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                        IconButton(onClick = {onEdit(ttask.Id)}) {Icon(Icons.Default.Edit, contentDescription = "Edit") }
                    }
                    Text(modifier = Modifier.padding(16.dp),
                        text = ttask.Date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (Expandable) {
                        Spacer(Modifier.height(8.dp))
                        Row() {
                            Text(
                                text = ttask.Description,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(9f),
                            )
                            IconButton(modifier = Modifier.weight(1f),onClick = { onDelete(ttask.Id) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete, // Use `Icons.Default`
                                    contentDescription = "Delete"
                                )
                            }

                        }

                    }
                }
            }
            Spacer(Modifier.height(5.dp))
        }
    }
}
@Composable
fun CompletedLazyListView(taskList: List<Task>, onTaskCompletedChange: (Task, Boolean) -> Unit) {

    LazyColumn(modifier = Modifier.wrapContentSize()) {
        items(taskList) { ttask ->
            var Expandable by remember { mutableStateOf(false) }
            Card(
                modifier = Modifier
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    .padding(8.dp)
                    .clickable { Expandable = !Expandable },
                elevation = CardDefaults.cardElevation(4.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
//                        .background(Color(0xFF5A636A))
                        .background(Color(0xff999999).copy(alpha = 0.8f))
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(modifier = Modifier.weight(1f),
                            checked = ttask.isCompleted.value,
                            onCheckedChange = { isChecked ->
                                ttask.isCompleted.value = isChecked
                            }
                        )


                        Text(
                            modifier = Modifier.weight(7f),
                            text = ttask.Title,
                            style = TextStyle(
                                textDecoration = TextDecoration.LineThrough, // Strike-through effect
                                fontWeight = FontWeight.Bold,
                                fontStyle = FontStyle.Italic,
                                fontSize = MaterialTheme.typography.titleMedium.fontSize
                            )
                        )
                        Text(modifier = Modifier.weight(2f),
                            text = ttask.Priority.name,
                            color = when (ttask.Priority) {
                                Priority.High -> Color(0xFFFFFFFF)
                                Priority.Medium -> Color(0xFF979797)
                                Priority.Low -> Color(0xFF121212)
                            },
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(modifier = Modifier.padding(16.dp),
                        text = ttask.Date,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    if (Expandable) {
                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = ttask.Description,
                            style = MaterialTheme.typography.bodyMedium
                        )

                    }
                }
            }
            Spacer(Modifier.height(5.dp))
        }
    }
}

//@Composable
//fun DailogBox(onCancel: () -> Unit,TaskList: SnapshotStateList<Task>){
//    var Title by remember { mutableStateOf("") }
//    var description by remember { mutableStateOf("N/A") }
//    var date by remember { mutableStateOf("01/01/2001") }
//    var Priority by remember{ mutableStateOf(Priority.High) }
//    Column(modifier = Modifier
//        .height(400.dp)
//        .width(350.dp)
//        .clip(RoundedCornerShape(topStart = 24.dp, bottomEnd = 24.dp))
//        .background(Color(0xFFCED2D3)), verticalArrangement = Arrangement.SpaceAround,horizontalAlignment = Alignment.CenterHorizontally) {
//        OutlinedTextField(value = Title,
//            keyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)
//            ,leadingIcon = { IconButton(modifier = Modifier.alpha(0.68f),onClick = {}){Icon(Icons.Default.Create, contentDescription = "Title")} },
//            trailingIcon = { IconButton(modifier = Modifier.alpha(0.68f),onClick = {Title=""}){Icon(Icons.Default.Delete, contentDescription = "clear")} }
//            , onValueChange = {Title=it}, label = { Text("Title")}, maxLines = 1)
//
//        OutlinedTextField(value = description,
//            keyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
//            leadingIcon = { IconButton(modifier = Modifier.alpha(0.68f),onClick = {}){Icon(Icons.Default.Edit, contentDescription = "Description")} },
//            trailingIcon = { IconButton(modifier = Modifier.alpha(0.68f),onClick = {description=""}){Icon(Icons.Default.Delete, contentDescription = "clear")} },
//            onValueChange = {description=it}, label = { Text("Description")},maxLines = 3)
//
//        OutlinedTextField(value = date,
//            keyboardOptions= KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
//            leadingIcon = { IconButton(modifier = Modifier.alpha(0.68f),onClick = {}){Icon(Icons.Default.DateRange, contentDescription = "Date")} },
//            trailingIcon = { IconButton(modifier = Modifier.alpha(0.68f),onClick = {date="01/01/2001"}){Icon(Icons.Default.Refresh, contentDescription = "clear")} }
//            ,onValueChange = {date=it}, label = { Text(text="Date")},maxLines = 1)
//
////        OutlinedTextField(value = priority,leadingIcon =
////        { IconButton(modifier = Modifier.alpha(0.68f),onClick = {}){Icon(Icons.Default.Star, contentDescription = "Priority")} } ,onValueChange = {priority=it}, label = { Text(text="Priority")},maxLines = 1)
//        PriorityDropdown { priority ->
//            Priority = priority // Update priority based on selection
//        }
//        Row(Modifier.fillMaxWidth(),horizontalArrangement = Arrangement.SpaceAround)     {
//            Button(onClick = {
//                var newTask=AddInList(Title,description,date, Priority)
//                TaskList.add(newTask);
//                onCancel()},modifier = Modifier.width(100.dp),colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF5A636A), // Background color
//                contentColor = Color(0xFFCED2D3) )) { Text("Save") }
//            Button(onClick = { onCancel() },modifier = Modifier.width(100.dp),colors = ButtonDefaults.buttonColors(
//                containerColor = Color(0xFF5A636A), // Background color
//                contentColor = Color(0xFFCED2D3) )) { Text("Cancel") }
//        }
//    }
//}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailogBox(onCancel: () -> Unit,TaskList: SnapshotStateList<Task>){
    var Title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("N/A") }
    var date by remember { mutableStateOf("01/01/2001") }
    var Priority by remember { mutableStateOf(Priority.High) }

    Dialog(onDismissRequest = { onCancel() }) { // Use Dialog to encapsulate the custom box
        Box(
            modifier = Modifier
                .height(400.dp)
                .width(350.dp)
                .background(Color(0xff2d4a53))
                .clip(RoundedCornerShape(topStart = 24.dp, bottomEnd = 24.dp))
        ) {
            // Background Image
//            Image(
//                painter = painterResource(id = R.drawable.tryimage), // Replace with your image resource ID
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier.fillMaxSize()
//            )

            // Foreground Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceAround,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = Title,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor =Color(0xffe5e5e5).copy(alpha = 0.2f) ,
                        unfocusedBorderColor = Color(0xffe6ede9),
                        focusedBorderColor = Color(0xffffffff)
                    )
                    ,
                    leadingIcon = {
                        IconButton(modifier = Modifier.alpha(0.68f), onClick = {}) {
                            Icon(Icons.Default.Create, contentDescription = "Title")
                        }
                    },
                    trailingIcon = {
                        IconButton(modifier = Modifier.alpha(0.68f), onClick = { Title = "" }) {
                            Icon(Icons.Default.Delete, contentDescription = "clear")
                        }
                    },
                    onValueChange = { Title = it },
                    label = { Text(text="Title",color= Color(0xffffffff)) },
                    maxLines = 1
                )

                OutlinedTextField(
                    value = description,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor =Color(0xffe5e5e5).copy(alpha = 0.2f) ,
                        unfocusedBorderColor = Color(0xffe6ede9),
                        focusedBorderColor = Color(0xffffffff)
                    ),
                    leadingIcon = {
                        IconButton(modifier = Modifier.alpha(0.68f), onClick = {}) {
                            Icon(Icons.Default.Edit, contentDescription = "Description")
                        }
                    },
                    trailingIcon = {
                        IconButton(modifier = Modifier.alpha(0.68f), onClick = { description = "" }) {
                            Icon(Icons.Default.Delete, contentDescription = "clear")
                        }
                    },
                    onValueChange = { description = it },
                    label = { Text(text ="Description",color=Color(0xffffffff)) },
                    maxLines = 3
                )

                OutlinedTextField(
                    value = date,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        containerColor =Color(0xffe5e5e5).copy(alpha = 0.2f) ,
                        unfocusedBorderColor = Color(0xffe6ede9),
                        focusedBorderColor = Color(0xffffffff)
                    ),
                    leadingIcon = {
                        IconButton(modifier = Modifier.alpha(0.68f), onClick = {}) {
                            Icon(Icons.Default.DateRange, contentDescription = "Date")
                        }
                    },
                    trailingIcon = {
                        IconButton(modifier = Modifier.alpha(0.68f), onClick = { date = "01/01/2001" }) {
                            Icon(Icons.Default.Refresh, contentDescription = "clear")
                        }
                    },
                    onValueChange = { date = it },
                    label = { Text(text = "Date",color=Color(0xffffffff)) },
                    maxLines = 1
                )

                PriorityDropdown { priority ->
                    Priority = priority // Update priority based on selection
                }
                Spacer(Modifier.height(25.dp))


                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(
                        onClick = {
                            val newTask = AddInList(Title, description, date, Priority)
                            TaskList.add(newTask)
                            onCancel()
                        },
                        modifier = Modifier
                            .width(100.dp)
                            .border(
                                width = 2.dp, //width of the border

                                color = Color(0xFFe5e5e5).copy(alpha = 0.6f),           // Color of the border
                                shape = RoundedCornerShape(12.dp) // Shape of the border
                            ),
//                        elevation = ButtonDefaults.buttonElevation(8.dp) ,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5A636A).copy(alpha=0.2f),
                            contentColor = Color(0xFFCED2D3)
                        )
                    ) {
                        Text("Save")
                    }
                    Spacer(Modifier.width(50.dp))

                    Button(
                        onClick = { onCancel() },
                        modifier = Modifier
                            .width(100.dp)
                            .border(
                                width = 2.dp,                // Width of the border
                                color = Color(0xFFe5e5e5).copy(alpha = 0.6f),           // Color of the border
                                shape = RoundedCornerShape(12.dp) // Shape of the border
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF5A636A).copy(alpha = 0.2f),
                            contentColor = Color(0xFFCED2D3),


                        )
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PriorityDropdown(selectedPriority: (Priority) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf(Priority.High) } // Default selected priority

    Box(modifier = Modifier.width(280.dp)) {
        // OutlinedTextField for displaying the selected priority
        OutlinedTextField(
            value = selectedOption.name, // Display the selected priority
            onValueChange = {},
            colors = TextFieldDefaults.outlinedTextFieldColors(
                containerColor =Color(0xffe5e5e5).copy(alpha = 0.2f) ,
                unfocusedBorderColor = Color(0xffe6ede9),
                focusedBorderColor = Color(0xffffffff)
            ), // Read-only, so no value change needed
            readOnly = true, // Prevent user typing
            leadingIcon = {
                IconButton(modifier = Modifier.alpha(0.68f), onClick = {}) {
                    Icon(Icons.Default.Star, contentDescription = "Date")
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true }, // Opens dropdown on click
            label = { Text(text="Priority",color=Color(0xffffffff)) },
            trailingIcon = { // Add a trailing icon to indicate dropdown
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = "Dropdown Icon",
                    Modifier.clickable { expanded = true } // Ensure trailing icon works
                )
            }
        )

        // DropdownMenu for showing priority options
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false } // Dismiss when clicked outside
        ) {
            Priority.values().forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name) },
                    onClick = {
                        selectedOption = priority // Update selected option
                        selectedPriority(priority) // Pass the selected priority to parent composable
                        expanded = false // Close the dropdown
                    }
                )
            }
        }
    }
}
fun AddInList(Title:String ,Description :String ,Date : String ,priority: Priority):Task{//this is for
    var newTask =Task(Id =TaskIdCounter,Title=Title,Description=Description,Date=Date,Priority=priority, isCompleted = mutableStateOf(false))
    TaskIdCounter++;
    return newTask;
}