-- =========================================================
-- V2: Configure Knowledge Chunk Vector Embeddings
-- =========================================================

-- The selected embedding model produces 1536-dimensional
-- embeddings.
--
-- Existing column:
--     vector
--
-- New column:
--     vector(1536)

ALTER TABLE knowledge_chunks
    ALTER COLUMN embedding TYPE vector(1536);

-- =========================================================
-- HNSW index for cosine similarity
-- =========================================================

CREATE INDEX idx_knowledge_chunks_embedding_hnsw
    ON knowledge_chunks
    USING hnsw (embedding vector_cosine_ops);