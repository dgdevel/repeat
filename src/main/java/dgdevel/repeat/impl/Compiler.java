package dgdevel.repeat.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import lombok.extern.log4j.Log4j;
import dgdevel.repeat.api.Context;
import dgdevel.repeat.api.OutputType;
import dgdevel.repeat.api.Template;
import dgdevel.repeat.api.exceptions.CompilerException;
import dgdevel.repeat.api.exceptions.MissingDependenciesException;
import dgdevel.repeat.api.exceptions.ParseException;
import dgdevel.repeat.api.exceptions.ResolveException;
import dgdevel.repeat.impl.textutils.Strings;

@Log4j
public class Compiler {

	private static ClassPool classPool = new ClassPool(true);

	private static void setStringContent(CtClass ctClass, String methodName, String javaSource) throws CompilerException {
		try {
			ctClass.getMethod(methodName, "()Ljava/lang/String;").setBody(javaSource);
		} catch (CannotCompileException e) {
			throw new CompilerException(javaSource, e);
		} catch (NotFoundException e) {
			throw new CompilerException(javaSource, e);
		}
	}

	public static Template compile(Context context, String requestedName) throws ParseException, CompilerException, ResolveException {
		Queue<String> queue = new LinkedList<String>();
		queue.add(requestedName);
		Map<String, CtClass> stubs = new HashMap<String, CtClass>();
		Template template = null;
		while (!queue.isEmpty()) {
			String name = queue.poll();
			log.trace("---- Compiler.compile: " + name);
			CtClass ctClass = null;
			String templateSource = context.resolver().resolve(name);
			if (!stubs.containsKey(name)) {
				try {
					ctClass = classPool.getCtClass(BaseTemplate.class.getName());
				} catch (NotFoundException e) {
					// cannot happen!
					throw new RuntimeException(e);
				}
				String className = Compiler.class.getPackage().getName() + ".generated." + Strings.randomClassName();
				ctClass.setName(className);
				context.registerGeneratedClassName(name, className);
				setStringContent(ctClass, "source", "{ return " + Strings.toJavaString(templateSource) + "; }");
			} else {
				ctClass = stubs.get(name);
			}
			String sbJavaSource = null, ioJavaSource = null;
			try {
				sbJavaSource = Transpiler.javaSource(templateSource, context.handlers(), OutputType.JAVA_LANG_STRINGBUILDER);
				ioJavaSource = Transpiler.javaSource(templateSource, context.handlers(), OutputType.JAVA_IO_WRITER);
			} catch (MissingDependenciesException e) {
				for (String dependency : e.dependencies()) {
					queue.add(dependency);
				}
				stubs.put(name, ctClass);
				queue.add(name);
				continue;
			}
			setStringContent(ctClass, "javaSourceStringBuilder", "{ return " + Strings.toJavaString(sbJavaSource) + "; }");
			setStringContent(ctClass, "javaSourceWriter", "{ return " + Strings.toJavaString(ioJavaSource) + "; }");
			try {
				ctClass.getMethod("run", "(Ljava/util/Map;Ljava/lang/StringBuilder;)V").setBody(sbJavaSource);
			} catch (CannotCompileException e) {
				throw new CompilerException(sbJavaSource, e);
			} catch (NotFoundException e) {
				throw new CompilerException(sbJavaSource, e);
			} 
			try {
				ctClass.getMethod("run", "(Ljava/util/Map;Ljava/io/Writer;)V").setBody(ioJavaSource);
			} catch (CannotCompileException e) {
				throw new CompilerException(ioJavaSource, e);
			} catch (NotFoundException e) {
				throw new CompilerException(ioJavaSource, e);
			} 
			try {
				@SuppressWarnings("unchecked") Class<Template> clazz = ctClass.toClass();
				template = clazz.newInstance();
				clazz.getMethod("instance", Template.class).invoke(template, template);
				context.put(name, template);
			} catch (CannotCompileException e) {
				throw new CompilerException(null, e);
			} catch (InstantiationException e) {
				throw new CompilerException(null, e);
			} catch (IllegalAccessException e) {
				throw new CompilerException(null, e);
			} catch (IllegalArgumentException e) {
				throw new CompilerException(null, e);
			} catch (InvocationTargetException e) {
				throw new CompilerException(null, e);
			} catch (NoSuchMethodException e) {
				throw new CompilerException(null, e);
			} catch (SecurityException e) {
				throw new CompilerException(null, e);
			}
		}
		return template;
	}

}
