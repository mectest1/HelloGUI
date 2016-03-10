/**
 * 
 */


function closeScene(/*Node*/node){
//	var scene = node && node.getScene();
//	scene && scene.getWindow().hide();
	var scene = node.getScene();
	if(null != scene){
		scene.getWindow().hide();
	}
}