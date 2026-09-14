package dev.apexstudios.ghostrenderer.api.level;

import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import org.jspecify.annotations.Nullable;

public interface DelegatedAttachmentHolder extends IAttachmentHolder {
    IAttachmentHolder delegate();

    @Override
    default boolean hasAttachments() {
        return delegate().hasAttachments();
    }

    @Override
    default boolean hasData(AttachmentType<?> type) {
        return delegate().hasData(type);
    }

    @Override
    default <T> T getData(AttachmentType<T> type) {
        return delegate().getData(type);
    }

    @Override
    default @Nullable <T> T getExistingDataOrNull(AttachmentType<T> type) {
        return delegate().getExistingDataOrNull(type);
    }

    @Override
    default @Nullable <T> T setData(AttachmentType<T> type, T data) {
        return delegate().setData(type, data);
    }

    @Override
    default @Nullable <T> T removeData(AttachmentType<T> type) {
        return delegate().removeData(type);
    }
}
