/**
 * @author rbellamy@terradatum.com
 * @date 8/23/16
 *
 * This method sets up the Maven and JDK tools, along with whatever other arbitrary environment
 * variables we passed in, and runs the body we passed in within that environment.
 */
def call(body) {
  // evaluate the body block, and collect configuration into the object
  def config = [:]
  body.resolveStrategy = Closure.DELEGATE_FIRST
  body.delegate = config
  body()

  String args = config.args
  String nodeName = config.nodeName

  // We're wrapping this in a timeout - if it takes more than 180 minutes, kill it.
  timeout(time: 180, unit: 'MINUTES') {
    //noinspection GroovyAssignabilityCheck
    withNodeEnv {
      nodeToolName = nodeName
      // Actually run groovy!
      cmd = {
        sh "gulp ${args}"
      }
    }
  }
}
