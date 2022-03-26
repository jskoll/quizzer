package quizzer.utility

enum class OS {
    WINDOWS, LINUX, MAC, SOLARIS
}

 fun getOS(): OS? {
    val os = System.getProperty("os.name").toLowerCase()
    return when {
        os.contains("win") -> {
            OS.WINDOWS
        }
        os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
            OS.LINUX
        }
        os.contains("mac") -> {
            OS.MAC
        }
        os.contains("sunos") -> {
            OS.SOLARIS
        }
        else -> null
    }
}

fun displayOS() {
    when (getOS()) {
        OS.WINDOWS -> println("Windows Operating System")
        OS.LINUX -> println("Linux Operating System")
        OS.MAC -> println("Mac Operating System")
        OS.SOLARIS -> println("Solaris Operating System")
        else -> println("Unknown Operating System")
    }
}