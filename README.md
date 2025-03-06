
## Topic covered 

### AndroidView

jetpack compose is a modern ui but sometime we need to use old androidviews like ImageView so to solve this problem AndroidView helps us to use this in compose
//101

### LaunchedEffect

Launched effect is a composable function that allows to run suspend function in the scope of composable fucntion and it will execute is asynchronously so it will also not effect the ui of the application

it can take attributes like key or lambda functions it can take more than one key at a time and can be used to do repetetive actions 


#### example:

To deploy this project run

```
    var trigger by remember { mutableStateOf(0) }

    LaunchedEffect(trigger) {
        delay(delayMillis) // Perform some asynchronous task
        println("Action triggered: $trigger")
        trigger++ // Increment the trigger to re-launch
    }
```
### enum class

The primary purpose of enum class is to predefine the named constant these constant represent the possible values that a varibale of than enum type can hold.

### data class
A data class in Kotlin is primarily used to create a type that holds multiple properties ``` (variables) ``` of different or similar data types. Its automatic generation of standard methods like ```equals()```, ```hashCode()```, ```toString()```, and ```copy()``` promotes cleaner, more concise, and easier-to-understand code, especially when representing data structures

### animateFloatAsState
animateFloatAsState primarily focus on animating a sigle float value 
#### attributes
1. targetValue: Float (Required): 
* This is the target float value that you want the animation to reach.
* When this value changes, the animation starts or restarts.
2. animationSpec (optional) :
* this parameter defines the behaviour of the animation 
* #### types of animationSpec:
    1. spring(dampingRatio: Float = Spring.DampingRatioNoBouncy, stiffness: Float = Spring.StiffnessMedium): Simulates a spring-like motion.
    2. tween(durationMillis: Int = DefaultTweenDuration, easing: Easing = LinearEasing): Creates a linear animation over a specified duration.
    3. keyframes(init: KeyframesSpec.Builder<Float>.() -> Unit): Creates an animation with defined values at specific points in time.
    4. infiniteRepeatable(animation: AnimationSpec<Float>, repeatMode: RepeatMode = RepeatMode.Restart): creates an animation that repeats infinitely.
    5. repeatable(iterations: Int, animation: AnimationSpec<Float>, repeatMode: RepeatMode = RepeatMode.Restart): creates an animation that repeats a defined number of times.
* the default is spring
3. finishedListener: ((Float) -> Unit)? = null (Optional):

* This is a lambda function that's called when the animation finishes.
* It receives the final animated float value as a parameter.
* It's useful for performing actions after the animation completes.

```
var targetScale by remember { mutableStateOf(1f) } // Initial target scale

    val animatedScale by animateFloatAsState(
        targetValue = targetScale, // Use targetScale here
        animationSpec = tween(500) // 500ms animation
    )

    Box(
        modifier = Modifier
            .size(100.dp)
            .background(Color.Green)
            .scale(animatedScale)
            .clickable {
                // Change the targetScale on click
                targetScale = if (targetScale == 1f) 1.5f else 1f
            }
    )
```

### Lottie Animation
dependencies
```
dependencies {
  implementation 'com.airbnb.android:lottie:$lottieVersion'
}
```
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



