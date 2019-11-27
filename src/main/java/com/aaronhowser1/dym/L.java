package com.aaronhowser1.dym;

import com.google.common.base.Preconditions;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.message.EntryMessage;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.MessageFactory;
import org.apache.logging.log4j.util.MessageSupplier;
import org.apache.logging.log4j.util.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@SuppressWarnings("WeakerAccess")
public final class L implements Logger {
    public enum DumpStackBehavior {
        FULL_DUMP,
        NORMAL_DUMP,
        DO_NOT_DUMP
    }

    private final Logger logger;

    private L(@Nonnull final String id, @Nonnull final String marker) {
        this.logger = LogManager.getLogger(id + (marker.trim().isEmpty()? "" : "/" + marker));
    }

    @Nonnull
    public static L create(@Nonnull final String id) {
        return create(id, "");
    }

    @Nonnull
    public static L create(@Nonnull final String id, @Nonnull final String marker) {
        return new L(Preconditions.checkNotNull(id), Preconditions.checkNotNull(marker));
    }

    public void logAndDump(@Nonnull final String message) {
        this.logAndDump(message, DumpStackBehavior.FULL_DUMP);
    }

    public void logAndDump(@Nonnull final String message, @Nonnull final DumpStackBehavior behavior) {
        this.doBigMessage(message, behavior, this::info);
    }

    public void bigWarn(@Nonnull final String message) {
        this.bigWarn(message, DumpStackBehavior.NORMAL_DUMP);
    }

    public void bigWarn(@Nonnull final String message, @Nonnull final DumpStackBehavior behavior) {
        this.doBigMessage(message, behavior, this::warn);
    }

    public void bigError(@Nonnull final String message) {
        this.bigError(message, DumpStackBehavior.NORMAL_DUMP);
    }

    public void bigError(@Nonnull final String message, @Nonnull final DumpStackBehavior behavior) {
        this.doBigMessage(message, behavior, this::error);
    }

    private void doBigMessage(@Nonnull final String message, @Nonnull final DumpStackBehavior dsb, @Nonnull final Consumer<String> logFun) {
        final StringBuilder messageBuilder = new StringBuilder();

        final List<String> lines = lines(replaceAllTabs(injectStartAndStopNewLine(addDumpIfNeeded(message, dsb))));
        final int maxLineLength = lines.stream().mapToInt(String::length).max().orElse(0);
        final int maxLength = maxLineLength + 4;

        IntStream.range(0, maxLength).forEach(i -> messageBuilder.append('*'));
        messageBuilder.append('\n');

        lines.forEach(it -> {
            messageBuilder.append("* ");
            messageBuilder.append(it);
            IntStream.range(0, -(it.length() - maxLineLength)).forEach(i -> messageBuilder.append(' '));
            messageBuilder.append(" *\n");
        });

        IntStream.range(0, maxLength).forEach(i -> messageBuilder.append('*'));

        final List<String> logLines = Arrays.asList(messageBuilder.toString().split(Pattern.quote("\n")));
        messageBuilder.delete(0, messageBuilder.length());
        logLines.forEach(logFun);
    }

    @Nonnull
    private static String injectStartAndStopNewLine(@Nonnull final String message) {
        return String.format(" \n%s\n ", message);
    }

    @Nonnull
    private static String replaceAllTabs(@Nonnull final String message) {
        return message.replace("\t", "    ");
    }

    @Nonnull
    private static List<String> lines(@Nonnull final String message) {
        return Arrays.asList(message.split(Pattern.quote("\n")));
    }

    @Nonnull
    private static String addDumpIfNeeded(@Nonnull final String message, @Nonnull final DumpStackBehavior dsb) {
        return dsb == DumpStackBehavior.DO_NOT_DUMP? message : doDump(message, dsb);
    }

    @Nonnull
    private static String doDump(@Nonnull final String initialString, @Nonnull final DumpStackBehavior dsb) {
        final StackTraceElement[] stack = new Throwable().getStackTrace();
        final StringBuilder builder = new StringBuilder();
        builder.append(initialString);
        builder.append("\n\n");
        final int start = dsb == DumpStackBehavior.FULL_DUMP? 5 : 0; // TODO Check if values are still correct
        final int endT = dsb == DumpStackBehavior.NORMAL_DUMP? Math.min(stack.length, 4) : stack.length;
        final int end = Math.min(start + endT, stack.length);
        IntStream.range(start, end).forEach(i -> {
            builder.append(toPrintableString(stack[i]));
            if (i != end - 1) builder.append('\n');
        });
        if (dsb == DumpStackBehavior.NORMAL_DUMP) builder.append("\n... (Rest of stack dump omitted)");
        return builder.toString();
    }

    @Nonnull
    private static String toPrintableString(@Nonnull final StackTraceElement element) {
        if (element.isNativeMethod()) {
            return String.format("at %s.%s (in JNI)", element.getClassName(), element.getMethodName());
        }
        return String.format("at %s.%s (%s:%s)", element.getClassName(), element.getMethodName(),
                element.getFileName() != null? element.getFileName() : "???", element.getLineNumber() < 0? "???" : ("" + element.getLineNumber()));
    }

    // And here the boilerplate code begins, just to implement the interface

    @Override
    public void catching(@Nonnull final Level level, @Nonnull final Throwable t) {
        this.logger.catching(level, t);
    }

    @Override
    public void catching(@Nonnull final Throwable t) {
        this.logger.catching(t);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final Message msg) {
        this.logger.debug(marker, msg);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final Message msg, @Nullable Throwable t) {
        this.logger.debug(marker, msg, t);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier) {
        this.logger.debug(marker, msgSupplier);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier, @Nullable final Throwable t) {
        this.logger.debug(marker, msgSupplier, t);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final CharSequence message) {
        this.logger.debug(marker, message);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.debug(marker, message, t);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final Object message) {
        this.logger.debug(marker, message);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.debug(marker, message, t);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message) {
        this.logger.debug(marker, message);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Object... params) {
        this.logger.debug(marker, message, params);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.debug(marker, message, paramSuppliers);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.debug(marker, message, t);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier) {
        this.logger.debug(marker, msgSupplier);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier, @Nullable final Throwable t) {
        this.logger.debug(marker, msgSupplier, t);
    }

    @Override
    public void debug(@Nonnull final Message msg) {
        this.logger.debug(msg);
    }

    @Override
    public void debug(@Nonnull final Message msg, @Nullable final Throwable t) {
        this.logger.debug(msg, t);
    }

    @Override
    public void debug(@Nonnull final MessageSupplier msgSupplier) {
        this.logger.debug(msgSupplier);
    }

    @Override
    public void debug(@Nonnull final MessageSupplier msgSupplier, @Nonnull final Throwable t) {
        this.logger.debug(msgSupplier, t);
    }

    @Override
    public void debug(@Nonnull final CharSequence message) {
        this.logger.debug(message);
    }

    @Override
    public void debug(@Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.debug(message, t);
    }

    @Override
    public void debug(@Nonnull final Object message) {
        this.logger.debug(message);
    }

    @Override
    public void debug(@Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.debug(message, t);
    }

    @Override
    public void debug(@Nonnull final String message) {
        this.logger.debug(message);
    }

    @Override
    public void debug(@Nonnull final String message, @Nonnull final Object... params) {
        this.logger.debug(message, params);
    }

    @Override
    public void debug(@Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.debug(message, paramSuppliers);
    }

    @Override
    public void debug(@Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.debug(message, t);
    }

    @Override
    public void debug(@Nonnull final Supplier<?> msgSupplier) {
        this.logger.debug(msgSupplier);
    }

    @Override
    public void debug(@Nonnull final Supplier<?> msgSupplier, @Nonnull final Throwable t) {
        this.logger.debug(msgSupplier, t);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0) {
        this.logger.debug(marker, message, p0);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.debug(marker, message, p0, p1);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.debug(marker, message, p0, p1, p2);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3) {
        this.logger.debug(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4) {
        this.logger.debug(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.debug(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.debug(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8) {
        this.logger.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void debug(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8, @Nullable final Object p9) {
        this.logger.debug(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0) {
        this.logger.debug(message, p0);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.debug(message, p0, p1);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.debug(message, p0, p1, p2);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        this.logger.debug(message, p0, p1, p2, p3);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4) {
        this.logger.debug(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.debug(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.debug(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.debug(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8) {
        this.logger.debug(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void debug(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8,
                      @Nullable final Object p9) {
        this.logger.debug(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Deprecated
    @Override
    public void entry() {
        this.logger.entry();
    }

    @Override
    public void entry(@Nonnull final Object... params) {
        this.logger.entry(params);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final Message msg) {
        this.logger.error(marker, msg);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final Message msg, @Nullable Throwable t) {
        this.logger.error(marker, msg, t);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier) {
        this.logger.error(marker, msgSupplier);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier, @Nullable final Throwable t) {
        this.logger.error(marker, msgSupplier, t);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final CharSequence message) {
        this.logger.error(marker, message);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.error(marker, message, t);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final Object message) {
        this.logger.error(marker, message);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.error(marker, message, t);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message) {
        this.logger.error(marker, message);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Object... params) {
        this.logger.error(marker, message, params);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.error(marker, message, paramSuppliers);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.error(marker, message, t);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier) {
        this.logger.error(marker, msgSupplier);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier, @Nullable final Throwable t) {
        this.logger.error(marker, msgSupplier, t);
    }

    @Override
    public void error(@Nonnull final Message msg) {
        this.logger.error(msg);
    }

    @Override
    public void error(@Nonnull final Message msg, @Nullable final Throwable t) {
        this.logger.error(msg, t);
    }

    @Override
    public void error(@Nonnull final MessageSupplier msgSupplier) {
        this.logger.error(msgSupplier);
    }

    @Override
    public void error(@Nonnull final MessageSupplier msgSupplier, @Nonnull final Throwable t) {
        this.logger.error(msgSupplier, t);
    }

    @Override
    public void error(@Nonnull final CharSequence message) {
        this.logger.error(message);
    }

    @Override
    public void error(@Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.error(message, t);
    }

    @Override
    public void error(@Nonnull final Object message) {
        this.logger.error(message);
    }

    @Override
    public void error(@Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.error(message, t);
    }

    @Override
    public void error(@Nonnull final String message) {
        this.logger.error(message);
    }

    @Override
    public void error(@Nonnull final String message, @Nonnull final Object... params) {
        this.logger.error(message, params);
    }

    @Override
    public void error(@Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.error(message, paramSuppliers);
    }

    @Override
    public void error(@Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.error(message, t);
    }

    @Override
    public void error(@Nonnull final Supplier<?> msgSupplier) {
        this.logger.error(msgSupplier);
    }

    @Override
    public void error(@Nonnull final Supplier<?> msgSupplier, @Nonnull final Throwable t) {
        this.logger.error(msgSupplier, t);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0) {
        this.logger.error(marker, message, p0);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.error(marker, message, p0, p1);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.error(marker, message, p0, p1, p2);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3) {
        this.logger.error(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4) {
        this.logger.error(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.error(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.error(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8) {
        this.logger.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void error(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8, @Nullable final Object p9) {
        this.logger.error(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0) {
        this.logger.error(message, p0);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.error(message, p0, p1);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.error(message, p0, p1, p2);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        this.logger.error(message, p0, p1, p2, p3);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4) {
        this.logger.error(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.error(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.error(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.error(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8) {
        this.logger.error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void error(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8,
                      @Nullable final Object p9) {
        this.logger.error(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Deprecated
    @Override
    public void exit() {
        this.logger.exit();
    }

    @Deprecated
    @Nullable
    @Override
    public <R> R exit(@Nullable final R result) {
        return this.logger.exit(result);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final Message msg) {
        this.logger.fatal(marker, msg);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final Message msg, @Nullable Throwable t) {
        this.logger.fatal(marker, msg, t);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier) {
        this.logger.fatal(marker, msgSupplier);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier, @Nullable final Throwable t) {
        this.logger.fatal(marker, msgSupplier, t);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final CharSequence message) {
        this.logger.fatal(marker, message);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.fatal(marker, message, t);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final Object message) {
        this.logger.fatal(marker, message);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.fatal(marker, message, t);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message) {
        this.logger.fatal(marker, message);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Object... params) {
        this.logger.fatal(marker, message, params);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.fatal(marker, message, paramSuppliers);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.fatal(marker, message, t);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier) {
        this.logger.fatal(marker, msgSupplier);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier, @Nullable final Throwable t) {
        this.logger.fatal(marker, msgSupplier, t);
    }

    @Override
    public void fatal(@Nonnull final Message msg) {
        this.logger.fatal(msg);
    }

    @Override
    public void fatal(@Nonnull final Message msg, @Nullable final Throwable t) {
        this.logger.fatal(msg, t);
    }

    @Override
    public void fatal(@Nonnull final MessageSupplier msgSupplier) {
        this.logger.fatal(msgSupplier);
    }

    @Override
    public void fatal(@Nonnull final MessageSupplier msgSupplier, @Nonnull final Throwable t) {
        this.logger.fatal(msgSupplier, t);
    }

    @Override
    public void fatal(@Nonnull final CharSequence message) {
        this.logger.fatal(message);
    }

    @Override
    public void fatal(@Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.fatal(message, t);
    }

    @Override
    public void fatal(@Nonnull final Object message) {
        this.logger.fatal(message);
    }

    @Override
    public void fatal(@Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.fatal(message, t);
    }

    @Override
    public void fatal(@Nonnull final String message) {
        this.logger.fatal(message);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nonnull final Object... params) {
        this.logger.fatal(message, params);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.fatal(message, paramSuppliers);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.fatal(message, t);
    }

    @Override
    public void fatal(@Nonnull final Supplier<?> msgSupplier) {
        this.logger.fatal(msgSupplier);
    }

    @Override
    public void fatal(@Nonnull final Supplier<?> msgSupplier, @Nonnull final Throwable t) {
        this.logger.fatal(msgSupplier, t);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0) {
        this.logger.fatal(marker, message, p0);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.fatal(marker, message, p0, p1);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.fatal(marker, message, p0, p1, p2);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3) {
        this.logger.fatal(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4) {
        this.logger.fatal(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.fatal(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8) {
        this.logger.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void fatal(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8, @Nullable final Object p9) {
        this.logger.fatal(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0) {
        this.logger.fatal(message, p0);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.fatal(message, p0, p1);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.fatal(message, p0, p1, p2);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        this.logger.fatal(message, p0, p1, p2, p3);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4) {
        this.logger.fatal(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.fatal(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.fatal(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8) {
        this.logger.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void fatal(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8,
                      @Nullable final Object p9) {
        this.logger.fatal(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Nonnull
    @Override
    public Level getLevel() {
        return this.logger.getLevel();
    }

    @Nonnull
    @Override
    public <MF extends MessageFactory> MF getMessageFactory() {
        return this.logger.getMessageFactory();
    }

    @Nonnull
    @Override
    public String getName() {
        return this.logger.getName();
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final Message msg) {
        this.logger.info(marker, msg);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final Message msg, @Nullable Throwable t) {
        this.logger.info(marker, msg, t);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier) {
        this.logger.info(marker, msgSupplier);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier, @Nullable final Throwable t) {
        this.logger.info(marker, msgSupplier, t);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final CharSequence message) {
        this.logger.info(marker, message);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.info(marker, message, t);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final Object message) {
        this.logger.info(marker, message);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.info(marker, message, t);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message) {
        this.logger.info(marker, message);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Object... params) {
        this.logger.info(marker, message, params);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.info(marker, message, paramSuppliers);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.info(marker, message, t);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier) {
        this.logger.info(marker, msgSupplier);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier, @Nullable final Throwable t) {
        this.logger.info(marker, msgSupplier, t);
    }

    @Override
    public void info(@Nonnull final Message msg) {
        this.logger.info(msg);
    }

    @Override
    public void info(@Nonnull final Message msg, @Nullable final Throwable t) {
        this.logger.info(msg, t);
    }

    @Override
    public void info(@Nonnull final MessageSupplier msgSupplier) {
        this.logger.info(msgSupplier);
    }

    @Override
    public void info(@Nonnull final MessageSupplier msgSupplier, @Nonnull final Throwable t) {
        this.logger.info(msgSupplier, t);
    }

    @Override
    public void info(@Nonnull final CharSequence message) {
        this.logger.info(message);
    }

    @Override
    public void info(@Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.info(message, t);
    }

    @Override
    public void info(@Nonnull final Object message) {
        this.logger.info(message);
    }

    @Override
    public void info(@Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.info(message, t);
    }

    @Override
    public void info(@Nonnull final String message) {
        this.logger.info(message);
    }

    @Override
    public void info(@Nonnull final String message, @Nonnull final Object... params) {
        this.logger.info(message, params);
    }

    @Override
    public void info(@Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.info(message, paramSuppliers);
    }

    @Override
    public void info(@Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.info(message, t);
    }

    @Override
    public void info(@Nonnull final Supplier<?> msgSupplier) {
        this.logger.info(msgSupplier);
    }

    @Override
    public void info(@Nonnull final Supplier<?> msgSupplier, @Nonnull final Throwable t) {
        this.logger.info(msgSupplier, t);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0) {
        this.logger.info(marker, message, p0);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.info(marker, message, p0, p1);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.info(marker, message, p0, p1, p2);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3) {
        this.logger.info(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4) {
        this.logger.info(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.info(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.info(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8) {
        this.logger.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void info(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8, @Nullable final Object p9) {
        this.logger.info(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0) {
        this.logger.info(message, p0);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.info(message, p0, p1);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.info(message, p0, p1, p2);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        this.logger.info(message, p0, p1, p2, p3);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4) {
        this.logger.info(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.info(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.info(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.info(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8) {
        this.logger.info(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void info(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8,
                      @Nullable final Object p9) {
        this.logger.info(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public boolean isDebugEnabled(@Nonnull final Marker marker) {
        return this.logger.isDebugEnabled(marker);
    }

    @Override
    public boolean isEnabled(@Nonnull final Level level) {
        return this.logger.isEnabled(level);
    }

    @Override
    public boolean isEnabled(@Nonnull final Level level, @Nonnull final Marker marker) {
        return this.logger.isEnabled(level, marker);
    }

    @Override
    public boolean isErrorEnabled() {
        return this.logger.isErrorEnabled();
    }

    @Override
    public boolean isErrorEnabled(@Nonnull final Marker marker) {
        return this.logger.isErrorEnabled(marker);
    }

    @Override
    public boolean isFatalEnabled() {
        return this.logger.isFatalEnabled();
    }

    @Override
    public boolean isFatalEnabled(@Nonnull final Marker marker) {
        return this.logger.isFatalEnabled(marker);
    }

    @Override
    public boolean isInfoEnabled() {
        return this.logger.isInfoEnabled();
    }

    @Override
    public boolean isInfoEnabled(@Nonnull final Marker marker) {
        return this.logger.isInfoEnabled(marker);
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override
    public boolean isTraceEnabled(@Nonnull final Marker marker) {
        return this.logger.isTraceEnabled(marker);
    }

    @Override
    public boolean isWarnEnabled() {
        return this.logger.isWarnEnabled();
    }

    @Override
    public boolean isWarnEnabled(@Nonnull final Marker marker) {
        return this.logger.isWarnEnabled(marker);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final Message msg) {
        this.logger.log(level, marker, msg);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final Message msg, @Nullable final Throwable t) {
        this.logger.log(level, marker, msg, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier) {
        this.logger.log(level, marker, msgSupplier);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier, @Nullable final Throwable t) {
        this.logger.log(level, marker, msgSupplier, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final CharSequence message) {
        this.logger.log(level, marker, message);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.log(level, marker, message, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final Object message) {
        this.logger.log(level, marker, message);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.log(level, marker, message, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message) {
        this.logger.log(level, marker, message);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Object... params) {
        this.logger.log(level, marker, message, params);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.log(level, marker, message, paramSuppliers);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.log(level, marker, message, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier) {
        this.logger.log(level, marker, msgSupplier);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier, @Nullable final Throwable t) {
        this.logger.log(level, marker, msgSupplier, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Message msg) {
        this.logger.log(level, msg);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Message msg, @Nullable final Throwable t) {
        this.logger.log(level, msg, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final MessageSupplier msgSupplier) {
        this.logger.log(level, msgSupplier);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final MessageSupplier msgSupplier, @Nonnull final Throwable t) {
        this.logger.log(level, msgSupplier, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final CharSequence message) {
        this.logger.log(level, message);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.log(level, message, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Object message) {
        this.logger.log(level, message);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.log(level, message, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message) {
        this.logger.log(level, message);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nonnull final Object... params) {
        this.logger.log(level, message, params);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.log(level, message, paramSuppliers);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.log(level, message, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Supplier<?> msgSupplier) {
        this.logger.log(level, msgSupplier);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Supplier<?> msgSupplier, @Nonnull final Throwable t) {
        this.logger.log(level, msgSupplier, t);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0) {
        this.logger.log(level, marker, message, p0);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.log(level, marker, message, p0, p1);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1,
                    @Nullable final Object p2) {
        this.logger.log(level, marker, message, p0, p1, p2);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1,
                    @Nullable final Object p2, @Nullable final Object p3) {
        this.logger.log(level, marker, message, p0, p1, p2, p3);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1,
                    @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4) {
        this.logger.log(level, marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1,
                    @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.log(level, marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1,
                    @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1,
                    @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6,
                    @Nullable final Object p7) {
        this.logger.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1,
                    @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6,
                    @Nullable final Object p7, @Nullable final Object p8) {
        this.logger.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1,
                    @Nullable final Object p2, @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6,
                    @Nullable final Object p7, @Nullable final Object p8, @Nullable final Object p9) {
        this.logger.log(level, marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0) {
        this.logger.log(level, message, p0);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.log(level, message, p0, p1);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.log(level, message, p0, p1, p2);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                    @Nullable final Object p3) {
        this.logger.log(level, message, p0, p1, p2, p3);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                    @Nullable final Object p3, @Nullable final Object p4) {
        this.logger.log(level, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                    @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.log(level, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                    @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.log(level, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                    @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                    @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                    @Nullable final Object p8) {
        this.logger.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void log(@Nonnull final Level level, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                    @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                    @Nullable final Object p8, @Nullable final Object p9) {
        this.logger.log(level, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void printf(@Nonnull final Level level, @Nonnull final Marker marker, @Nonnull final String format, @Nonnull final Object... params) {
        this.logger.printf(level, marker, format, params);
    }

    @Override
    public void printf(@Nonnull final Level level, @Nonnull final String format, @Nonnull final Object... params) {
        this.logger.printf(level, format, params);
    }

    @Nonnull
    @Override
    public <T extends Throwable> T throwing(@Nonnull final Level level, @Nonnull final T t) {
        return this.logger.throwing(level, t);
    }

    @Nonnull
    @Override
    public <T extends Throwable> T throwing(@Nonnull final T t) {
        return this.logger.throwing(t);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final Message msg) {
        this.logger.trace(marker, msg);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final Message msg, @Nullable Throwable t) {
        this.logger.trace(marker, msg, t);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier) {
        this.logger.trace(marker, msgSupplier);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier, @Nullable final Throwable t) {
        this.logger.trace(marker, msgSupplier, t);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final CharSequence message) {
        this.logger.trace(marker, message);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.trace(marker, message, t);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final Object message) {
        this.logger.trace(marker, message);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.trace(marker, message, t);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message) {
        this.logger.trace(marker, message);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Object... params) {
        this.logger.trace(marker, message, params);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.trace(marker, message, paramSuppliers);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.trace(marker, message, t);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier) {
        this.logger.trace(marker, msgSupplier);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier, @Nullable final Throwable t) {
        this.logger.trace(marker, msgSupplier, t);
    }

    @Override
    public void trace(@Nonnull final Message msg) {
        this.logger.trace(msg);
    }

    @Override
    public void trace(@Nonnull final Message msg, @Nullable final Throwable t) {
        this.logger.trace(msg, t);
    }

    @Override
    public void trace(@Nonnull final MessageSupplier msgSupplier) {
        this.logger.trace(msgSupplier);
    }

    @Override
    public void trace(@Nonnull final MessageSupplier msgSupplier, @Nonnull final Throwable t) {
        this.logger.trace(msgSupplier, t);
    }

    @Override
    public void trace(@Nonnull final CharSequence message) {
        this.logger.trace(message);
    }

    @Override
    public void trace(@Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.trace(message, t);
    }

    @Override
    public void trace(@Nonnull final Object message) {
        this.logger.trace(message);
    }

    @Override
    public void trace(@Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.trace(message, t);
    }

    @Override
    public void trace(@Nonnull final String message) {
        this.logger.trace(message);
    }

    @Override
    public void trace(@Nonnull final String message, @Nonnull final Object... params) {
        this.logger.trace(message, params);
    }

    @Override
    public void trace(@Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.trace(message, paramSuppliers);
    }

    @Override
    public void trace(@Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.trace(message, t);
    }

    @Override
    public void trace(@Nonnull final Supplier<?> msgSupplier) {
        this.logger.trace(msgSupplier);
    }

    @Override
    public void trace(@Nonnull final Supplier<?> msgSupplier, @Nonnull final Throwable t) {
        this.logger.trace(msgSupplier, t);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0) {
        this.logger.trace(marker, message, p0);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.trace(marker, message, p0, p1);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.trace(marker, message, p0, p1, p2);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                     @Nullable final Object p3) {
        this.logger.trace(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                     @Nullable final Object p3, @Nullable final Object p4) {
        this.logger.trace(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                     @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.trace(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                     @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.trace(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                     @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                     @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                     @Nullable final Object p8) {
        this.logger.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void trace(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                     @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                     @Nullable final Object p8, @Nullable final Object p9) {
        this.logger.trace(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0) {
        this.logger.trace(message, p0);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.trace(message, p0, p1);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.trace(message, p0, p1, p2);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        this.logger.trace(message, p0, p1, p2, p3);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                     @Nullable final Object p4) {
        this.logger.trace(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                     @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.trace(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                     @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.trace(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                     @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.trace(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                     @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8) {
        this.logger.trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void trace(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                     @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8,
                     @Nullable final Object p9) {
        this.logger.trace(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Nonnull
    @Override
    public EntryMessage traceEntry() {
        return this.logger.traceEntry();
    }

    @Nonnull
    @Override
    public EntryMessage traceEntry(@Nonnull final String format, @Nonnull final Object... params) {
        return this.logger.traceEntry(format, params);
    }

    @Nonnull
    @Override
    public EntryMessage traceEntry(@Nonnull final Supplier<?>... paramSuppliers) {
        return this.logger.traceEntry(paramSuppliers);
    }

    @Override
    public EntryMessage traceEntry(@Nonnull final String format, @Nonnull final Supplier<?>... paramSuppliers) {
        return this.logger.traceEntry(format, paramSuppliers);
    }

    @Nonnull
    @Override
    public EntryMessage traceEntry(@Nonnull final Message message) {
        return this.logger.traceEntry(message);
    }

    @Override
    public void traceExit() {
        this.logger.traceExit();
    }

    @Nullable
    @Override
    public <R> R traceExit(@Nullable final R result) {
        return this.logger.traceExit(result);
    }

    @Nullable
    @Override
    public <R> R traceExit(@Nonnull final String format, @Nullable final R result) {
        return this.logger.traceExit(format, result);
    }

    @Override
    public void traceExit(@Nonnull final EntryMessage message) {
        this.logger.traceExit(message);
    }

    @Nullable
    @Override
    public <R> R traceExit(@Nonnull final EntryMessage message, @Nullable final R result) {
        return this.logger.traceExit(message, result);
    }

    @Nullable
    @Override
    public <R> R traceExit(@Nonnull final Message message, @Nullable final R result) {
        return this.logger.traceExit(message, result);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final Message msg) {
        this.logger.warn(marker, msg);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final Message msg, @Nullable Throwable t) {
        this.logger.warn(marker, msg, t);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier) {
        this.logger.warn(marker, msgSupplier);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final MessageSupplier msgSupplier, @Nullable final Throwable t) {
        this.logger.warn(marker, msgSupplier, t);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final CharSequence message) {
        this.logger.warn(marker, message);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.warn(marker, message, t);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final Object message) {
        this.logger.warn(marker, message);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.warn(marker, message, t);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message) {
        this.logger.warn(marker, message);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Object... params) {
        this.logger.warn(marker, message, params);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.warn(marker, message, paramSuppliers);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.warn(marker, message, t);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier) {
        this.logger.warn(marker, msgSupplier);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final Supplier<?> msgSupplier, @Nullable final Throwable t) {
        this.logger.warn(marker, msgSupplier, t);
    }

    @Override
    public void warn(@Nonnull final Message msg) {
        this.logger.warn(msg);
    }

    @Override
    public void warn(@Nonnull final Message msg, @Nullable final Throwable t) {
        this.logger.warn(msg, t);
    }

    @Override
    public void warn(@Nonnull final MessageSupplier msgSupplier) {
        this.logger.warn(msgSupplier);
    }

    @Override
    public void warn(@Nonnull final MessageSupplier msgSupplier, @Nonnull final Throwable t) {
        this.logger.warn(msgSupplier, t);
    }

    @Override
    public void warn(@Nonnull final CharSequence message) {
        this.logger.warn(message);
    }

    @Override
    public void warn(@Nonnull final CharSequence message, @Nonnull final Throwable t) {
        this.logger.warn(message, t);
    }

    @Override
    public void warn(@Nonnull final Object message) {
        this.logger.warn(message);
    }

    @Override
    public void warn(@Nonnull final Object message, @Nonnull final Throwable t) {
        this.logger.warn(message, t);
    }

    @Override
    public void warn(@Nonnull final String message) {
        this.logger.warn(message);
    }

    @Override
    public void warn(@Nonnull final String message, @Nonnull final Object... params) {
        this.logger.warn(message, params);
    }

    @Override
    public void warn(@Nonnull final String message, @Nonnull final Supplier<?>... paramSuppliers) {
        this.logger.warn(message, paramSuppliers);
    }

    @Override
    public void warn(@Nonnull final String message, @Nonnull final Throwable t) {
        this.logger.warn(message, t);
    }

    @Override
    public void warn(@Nonnull final Supplier<?> msgSupplier) {
        this.logger.warn(msgSupplier);
    }

    @Override
    public void warn(@Nonnull final Supplier<?> msgSupplier, @Nonnull final Throwable t) {
        this.logger.warn(msgSupplier, t);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0) {
        this.logger.warn(marker, message, p0);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.warn(marker, message, p0, p1);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.warn(marker, message, p0, p1, p2);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3) {
        this.logger.warn(marker, message, p0, p1, p2, p3);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4) {
        this.logger.warn(marker, message, p0, p1, p2, p3, p4);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.warn(marker, message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.warn(marker, message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8) {
        this.logger.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void warn(@Nonnull final Marker marker, @Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2,
                      @Nullable final Object p3, @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7,
                      @Nullable final Object p8, @Nullable final Object p9) {
        this.logger.warn(marker, message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0) {
        this.logger.warn(message, p0);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1) {
        this.logger.warn(message, p0, p1);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2) {
        this.logger.warn(message, p0, p1, p2);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3) {
        this.logger.warn(message, p0, p1, p2, p3);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4) {
        this.logger.warn(message, p0, p1, p2, p3, p4);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5) {
        this.logger.warn(message, p0, p1, p2, p3, p4, p5);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6) {
        this.logger.warn(message, p0, p1, p2, p3, p4, p5, p6);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7) {
        this.logger.warn(message, p0, p1, p2, p3, p4, p5, p6, p7);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8) {
        this.logger.warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8);
    }

    @Override
    public void warn(@Nonnull final String message, @Nullable final Object p0, @Nullable final Object p1, @Nullable final Object p2, @Nullable final Object p3,
                      @Nullable final Object p4, @Nullable final Object p5, @Nullable final Object p6, @Nullable final Object p7, @Nullable final Object p8,
                      @Nullable final Object p9) {
        this.logger.warn(message, p0, p1, p2, p3, p4, p5, p6, p7, p8, p9);
    }
}
